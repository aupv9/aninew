package autogear.frontapi.storage

import org.springframework.web.multipart.MultipartFile
import java.time.Duration

interface StorageProvider {
    fun store(key: String, content: MultipartFile?)
    fun retrieve(key: String): Any?
    fun delete(key: String)
    fun exits(key: String): Boolean
    fun generatePresignedUrl( key: String, duration: Duration): String
}