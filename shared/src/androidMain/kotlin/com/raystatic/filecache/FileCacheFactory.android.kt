package com.raystatic.filecache

import android.content.Context
import kotlin.time.Duration

lateinit var fileCacheContext: Context

fun FileCacheInit(context: Context) {
    fileCacheContext = context
}

actual object FileCacheFactory {
    actual fun create(maxSizeMB: Int, defaultTTL: Duration): FileCache {
        val store = AndroidFileStore(fileCacheContext)
        return FileCacheImpl(store, maxSizeMB * 1024L * 1024L, defaultTTL)
    }
}

