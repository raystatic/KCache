package com.raystatic.filecache

import kotlinx.cinterop.*
import platform.Foundation.*

class IOSFileStore : FileStore {
    private val dir: NSURL = NSFileManager.defaultManager.URLForDirectory(
        directory = NSCachesDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null
    )!!

    private fun fileUrl(key: String): NSURL = dir.URLByAppendingPathComponent(key)!!

    override fun exists(key: String): Boolean = NSFileManager.defaultManager.fileExistsAtPath(fileUrl(key).path!!)

    override fun read(key: String): ByteArray? {
        val data = NSData.dataWithContentsOfURL(fileUrl(key)) ?: return null
        val length = data.length.toInt()
        return ByteArray(length).apply {
            usePinned { pinned ->
                data.bytes?.let { ptr ->
                    memcpy(pinned.addressOf(0), ptr, length.convert())
                }
            }
        }
    }

    override fun write(key: String, data: ByteArray) {
        data.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = data.size.toULong())
                ?.writeToURL(fileUrl(key), atomically = true)
        }
    }

    override fun delete(key: String) {
        NSFileManager.defaultManager.removeItemAtURL(fileUrl(key), null)
    }

    override fun listFiles(): List<FileInfo> = emptyList()
}

