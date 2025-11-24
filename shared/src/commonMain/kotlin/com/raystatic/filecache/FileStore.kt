package com.raystatic.filecache

interface FileStore {
    fun exists(key: String): Boolean
    fun read(key: String): ByteArray?
    fun write(key: String, data: ByteArray)
    fun delete(key: String)
    fun listFiles(): List<FileInfo>
}

data class FileInfo(val name: String, val size: Long, val lastModified: Long)

