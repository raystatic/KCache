package com.raystatic.filecache

import java.io.File

class JvmFileStore : FileStore {
    private val dir = File("kmp_cache_jvm").apply { mkdirs() }

    override fun exists(key: String): Boolean = File(dir, key).exists()

    override fun read(key: String): ByteArray? = File(dir, key).takeIf { it.exists() }?.readBytes()

    override fun write(key: String, data: ByteArray) {
        File(dir, key).writeBytes(data)
    }

    override fun delete(key: String) {
        File(dir, key).delete()
    }

    override fun listFiles(): List<FileInfo> =
        dir.listFiles()?.map { FileInfo(it.name, it.length(), it.lastModified()) } ?: emptyList()
}

