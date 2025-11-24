package com.raystatic.filecache

import kotlin.time.Duration

interface FileCache {
    suspend fun put(key: String, data: ByteArray, ttl: Duration? = null)
    suspend fun get(key: String): ByteArray?
    suspend fun remove(key: String)
    suspend fun clear()
}

