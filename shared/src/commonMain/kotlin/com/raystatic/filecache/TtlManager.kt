package com.raystatic.filecache

import kotlinx.datetime.Clock

class TtlManager {
    fun isExpired(entry: CacheEntry): Boolean {
        val now = Clock.System.now().epochSeconds
        return entry.ttlSeconds != null && now > entry.createdAt + entry.ttlSeconds
    }
}

