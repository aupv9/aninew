package storage.connect.config.cloudflare.aws

import aws.sdk.kotlin.services.s3.model.CreateBucketRequest
import aws.sdk.kotlin.services.s3.model.HeadBucketRequest
import aws.smithy.kotlin.runtime.net.url.Url
import storage.connect.config.cloudflare.CloudflareConfig
import storage.connect.config.cloudflare.OperationWithCF

class S3Operation : OperationWithCF, CloudflareConfig {
    private var configInfoAWS: ConfigInfoAWS? = null

    override suspend fun createBucket(bucketName: String, mapInfo: Map<String, Any>) {

        configInfoAWS = this@S3Operation.createConfigInfoForClient(mapInfo)
        val s3ConfigClient = S3ConfigClient()
        configInfoAWS?.let { s3ConfigClient.setConfigInfoS3(it) }

        val s3Client = s3ConfigClient.getS3Client()
//        if (existBucket(s3ConfigClient, bucketName)) return
        val request = CreateBucketRequest{
            bucket = bucketName
        }
        s3Client.createBucket(request)
    }

    private suspend fun existBucket(s3ConfigClient : S3ConfigClient, bucketName: String): Boolean {
        val s3Client = s3ConfigClient.getS3Client()
        return try {
            val headBucketRequest = HeadBucketRequest {
                bucket = bucketName
                expectedBucketOwner = configInfoAWS?.accountId
            }
            s3Client.headBucket(headBucketRequest)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteBucket(bucketName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun putObject(bucketName: String, objectName: String, content: ByteArray) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteObject(bucketName: String, objectName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getObject(bucketName: String, objectName: String): ByteArray {
        TODO("Not yet implemented")
    }

    override suspend fun listObjects(bucketName: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun listBuckets(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteObjects(bucketName: String, objectNames: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun createBucketIfNotExists(bucketName: String) {
        TODO("Not yet implemented")
    }

    override fun createConfigInfoForClient(mapInfo: Map<String, Any>): ConfigInfoAWS? {
        this@S3Operation.configInfoAWS = ConfigInfoAWS()
        mapInfo.forEach{
            when(it.key){
                "access_key_id" -> this@S3Operation.configInfoAWS?.accessKeyId = it.value as String
                "secret_access_key" -> this@S3Operation.configInfoAWS?.secretAccessKey = it.value as String
                "region" ->  this@S3Operation.configInfoAWS?.region = it.value as String
                "endpoint_url" ->  this@S3Operation.configInfoAWS?.endpointUrl = Url.parse(it.value as String)
            }
        }
        return configInfoAWS
    }
}