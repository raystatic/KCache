package com.raystatic.filecache

import kotlinx.datetime.Clock
import kotlin.time.Duration

class FileCacheImpl(
    private val store: FileStore,
    private val maxSizeBytes: Long,
    private val defaultTTL: Duration
) : FileCache {

    private var index: MutableMap<String, CacheEntry> = loadIndex()
    private val lru = LruManager(maxSizeBytes)
    private val ttl = TtlManager()

    private fun loadIndex(): MutableMap<String, CacheEntry> {
        return store.read("__index__")?.let { bytes ->
            CacheIndexSerializer.deserialize(bytes)
        } ?: mutableMapOf()
    }

    override suspend fun put(key: String, data: ByteArray, ttl: Duration?) {
        val now = Clock.System.now().epochSeconds

        index[key] = CacheEntry(
            key = key,
            createdAt = now,
            lastAccess = now,
            size = data.size.toLong(),
            ttlSeconds = (ttl ?: defaultTTL).inWholeSeconds
        )

        store.write(key, data)
        persistIndex()

        lru.evict(index) {
            store.delete(it)
        }
        persistIndex()
    }

    override suspend fun get(key: String): ByteArray? {
        val entry = index[key] ?: return null

        if (ttl.isExpired(entry)) {
            remove(key)
            return null
        }

        val updated = entry.copy(lastAccess = Clock.System.now().epochSeconds)
        index[key] = updated
        persistIndex()

        return store.read(key)
    }

    override suspend fun remove(key: String) {
        store.delete(key)
        index.remove(key)
        persistIndex()
    }

    override suspend fun clear() {
        index.keys.forEach { store.delete(it) }
        index.clear()
        persistIndex()
    }

    private fun persistIndex() {
        store.write("__index__", CacheIndexSerializer.serialize(index))
    }
}

