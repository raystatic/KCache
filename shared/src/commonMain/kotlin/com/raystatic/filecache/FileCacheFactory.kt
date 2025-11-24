package com.raystatic.filecache

import kotlin.time.Duration

expect object FileCacheFactory {
    fun create(maxSizeMB: Int = 20, defaultTTL: Duration = Duration.parse("30m")): FileCache
}

