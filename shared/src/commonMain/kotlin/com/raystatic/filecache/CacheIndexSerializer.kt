package com.raystatic.filecache

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CacheIndexSerializable(
    val items: Map<String, CacheEntry>
)

object CacheIndexSerializer {
    private val json = Json { prettyPrint = true }

    fun serialize(map: Map<String, CacheEntry>): ByteArray =
        json.encodeToString(CacheIndexSerializable.serializer(), CacheIndexSerializable(map)).encodeToByteArray()

    fun deserialize(bytes: ByteArray): MutableMap<String, CacheEntry> =
        try {
            json.decodeFromString(CacheIndexSerializable.serializer(), bytes.decodeToString()).items.toMutableMap()
        } catch (e: Exception) {
            mutableMapOf()
        }
}

