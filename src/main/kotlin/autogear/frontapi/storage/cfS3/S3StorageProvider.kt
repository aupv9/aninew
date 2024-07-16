package autogear.frontapi.storage.cfS3

import autogear.frontapi.service.S3Service
import autogear.frontapi.storage.StorageProvider
import java.time.Duration

class S3StorageProvider(
    private val s3Service: S3Service,
    private var bucketName: String
): StorageProvider {


    override fun store(key: String, content: ByteArray) {
        s3Service.uploadFile(bucketName, key, content)
    }

    override fun retrieve(key: String): Any? {
        return s3Service.getObject(bucketName, key)
    }

    override fun delete(key: String) {
        TODO("Not yet implemented")
    }

    override fun exits(key: String): Boolean {
        return s3Service.getHeadObject(bucketName, key) != null
    }

    override fun generatePresignedUrl(key: String, duration: Duration): String {
        return s3Service.generatePresignedUrl(bucketName, key, duration)
    }
}