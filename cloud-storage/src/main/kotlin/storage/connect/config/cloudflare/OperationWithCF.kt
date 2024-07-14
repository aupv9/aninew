package storage.connect.config.cloudflare


interface OperationWithCF {
    suspend fun createBucket(bucketName: String, mapInfo: Map<String, Any>)
    suspend fun deleteBucket(bucketName: String)
    suspend fun putObject(bucketName: String, objectName: String, content: ByteArray)

    suspend fun deleteObject(bucketName: String, objectName: String)

    suspend fun getObject(bucketName: String, objectName: String): ByteArray
    suspend fun listObjects(bucketName: String): List<String>
    suspend fun listBuckets(): List<String>
    suspend fun deleteObjects(bucketName: String, objectNames: List<String>)
    suspend fun createBucketIfNotExists(bucketName: String)
}