package autogear.frontapi.service

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.configuration.custom.CustomerFilePropsCredentialsProvider
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.core.exception.SdkException
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.io.File
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration

@Service
class S3Service(
    private var autoGearConfiguration: AutoGearConfiguration,
    private val customerFilePropsCredentialsProvider: CustomerFilePropsCredentialsProvider

) {
    private final val logger = org.slf4j.LoggerFactory.getLogger(javaClass)

    private lateinit var s3Client: S3Client

    init {
        init()
    }

      private final fun init() {

        s3Client = S3Client.builder()
            .region(Region.of(autoGearConfiguration.cfConfiguration.region))
            .credentialsProvider(customerFilePropsCredentialsProvider)
            .endpointOverride(URI.create(autoGearConfiguration.cfConfiguration.endPoint))
            .serviceConfiguration {
                it.pathStyleAccessEnabled(true)
            }
            .build()
    }

    fun getHeadObject(bucketName: String, objectKey: String): HeadObjectResponse? = try {
        s3Client.headObject { it.bucket(bucketName).key(objectKey) }
    }catch (e: SdkException ){
        logger.error("Error getting object: {}", e.message)
        null
    }

    fun doesBucketExist(bucketName: String): Boolean{
        val headBucketRequest = HeadBucketRequest.builder().bucket(bucketName)
            .build()
        try {
            s3Client.headBucket(headBucketRequest)
            return true
        } catch (e: Exception) {
            return false
        }
    }
//
//    fun createFolder(bucketName: String, folderName: String): PutObjectResponse {
//        val folderKey = if (folderName.endsWith("/")) folderName else "$folderName"
//
//        val putObjectRequest = PutObjectRequest.builder()
//            .bucket(bucketName)
//            .key(folderKey)
//            .build()
//
//        return s3Client.putObject(putObjectRequest, RequestBody.empty())
//    }


    fun uploadFile(bucketName: String, key: String, file: MultipartFile?): PutObjectResponse? {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return try {
            val requestBody = if(file == null ) RequestBody.empty() else RequestBody.fromBytes(file.bytes)
            s3Client.putObject(putObjectRequest, requestBody)
        }catch (e: SdkException ){
            logger.error("Error creating folder: {}", e.message)
            null
        }
    }

    fun createBucket(bucketName: String): CreateBucketResponse?  = try {
        s3Client.createBucket { it.bucket(bucketName) }
    }catch (e: SdkException ){
        logger.error("Error creating bucket: {}", e.message)
        null
    }

    fun deleteBucket(bucketName: String): DeleteBucketResponse? = try {
        s3Client.deleteBucket { it.bucket(bucketName) }
    }catch (e: SdkException){
        logger.error("Error deleting bucket: {}", e.message)
        null
    }


    fun putObject(bucketName: String, key: String, file: File) {
        s3Client.putObject(
            { it.bucket(bucketName).key(key) },
            file.toPath()
        )
    }

    fun listObjects(bucketName: String) {
        val response = s3Client.listObjectsV2 { it.bucket(bucketName) }
        response.contents().forEach { println(it.key()) }
    }

    fun getObject(bucketName: String, key: String): GetObjectResponse? = try {
        s3Client.getObject { it.bucket(bucketName).key(key) }.response()
    }catch (e: SdkException ){
        logger.error("Error get object: {}", e.message)
        null
    }

    fun uploadMultiPart(bucketName: String, key: String, file: File) {
        s3Client.putObject(
            { it.bucket(bucketName).key(key) },
            file.toPath()
        )
    }

    fun generatePresignedUrl(bucketName: String, objectKey: String, duration: Duration): String = try {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(duration)
            .getObjectRequest(getObjectRequest)
            .build()

        val presigner = S3Presigner.builder()
            .region(Region.of(autoGearConfiguration.cfConfiguration.region))
            .credentialsProvider(ProfileCredentialsProvider.create())
            .endpointOverride(URI.create(autoGearConfiguration.cfConfiguration.endPoint))
            .build()

        val presignedGetObjectRequest = presigner.presignGetObject(presignRequest)

        presignedGetObjectRequest.url().toExternalForm()
    }catch (e: SdkException ){
        logger.error("Error get object: {}", e.message)
        ""
    }
}