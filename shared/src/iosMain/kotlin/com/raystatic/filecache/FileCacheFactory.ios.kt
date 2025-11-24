package com.raystatic.filecache

import kotlin.time.Duration

actual object FileCacheFactory {
    actual fun create(maxSizeMB: Int, defaultTTL: Duration): FileCache {
        return FileCacheImpl(IOSFileStore(), maxSizeMB * 1024L * 1024L, defaultTTL)
    }
}

