package com.raystatic.filecache

import kotlinx.serialization.Serializable

@Serializable
data class CacheEntry(
    val key: String,
    val createdAt: Long,
    val lastAccess: Long,
    val size: Long,
    val ttlSeconds: Long? = null
)

