import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.createBucket
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.net.url.Url

suspend fun main() {
    val s3 = S3Client {
        region = "auto"
        endpointUrl = Url.parse("https://22bc2eb698766d70fb0ff8cdc8456e32.r2.cloudflarestorage.com")
        credentialsProvider = StaticCredentialsProvider {
            accessKeyId = "4c10586062c9074dc97ff4e4d0573223"
            secretAccessKey = "35da34514d6166e2fec76298d36e0f16add5f1201e7778de8ad183b24999577e"
        }
    }

    val res = s3.createBucket {
        bucket = "first-bucket12"
    }

    println(res)

    val request = PutObjectRequest {
        bucket = "first-bucket12"
        key = "cf"
        body = ByteStream.fromString("Hello, World!")
    }


    s3.putObject(request)
    println("Object uploaded successfully!")
}
