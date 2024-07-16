package autogear.frontapi.storage

import java.time.Duration

interface StorageProvider {
    fun store(key: String, content: ByteArray)
    fun retrieve(key: String): Any?
    fun delete(key: String)
    fun exits(key: String): Boolean
    fun generatePresignedUrl( key: String, duration: Duration): String
}