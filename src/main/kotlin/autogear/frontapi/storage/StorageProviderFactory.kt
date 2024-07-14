package autogear.frontapi.storage

import autogear.frontapi.service.S3Service
import autogear.frontapi.storage.cfS3.S3StorageProvider

class StorageProviderFactory {
    companion object {
        fun createStorageProvider(type: StorageType, s3Service: S3Service, bucketName: String): StorageProvider {
            return when (type) {
                StorageType.S3 -> S3StorageProvider(s3Service, bucketName)
//                StorageType.Local -> LocalStorageProvider()
               else -> throw IllegalArgumentException("Invalid storage type")
            }
        }
    }
}

enum class StorageType{

    S3,
    Local
}