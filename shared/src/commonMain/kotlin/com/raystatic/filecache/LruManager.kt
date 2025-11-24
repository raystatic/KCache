package com.raystatic.filecache

class LruManager(private val maxSizeBytes: Long) {
    fun evict(index: MutableMap<String, CacheEntry>, delete: (String) -> Unit) {
        var total = index.values.sumOf { it.size }
        if (total <= maxSizeBytes) return

        val sorted = index.values.sortedBy { it.lastAccess }
        for (entry in sorted) {
            delete(entry.key)
            index.remove(entry.key)
            total = index.values.sumOf { it.size }
            if (total <= maxSizeBytes) break
        }
    }
}

