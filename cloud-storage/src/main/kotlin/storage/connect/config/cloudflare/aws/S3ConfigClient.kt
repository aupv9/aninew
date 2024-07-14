package storage.connect.config.cloudflare.aws

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client

class S3ConfigClient {

    private var s3Client: S3Client? = null
    private var config: ConfigInfoAWS? = null
    fun getS3Client(): S3Client {
        if (s3Client == null) {
            s3Client = S3Client {
                region = config!!.region
                credentialsProvider = StaticCredentialsProvider {
                    accessKeyId = config!!.accessKeyId
                    secretAccessKey = config!!.secretAccessKey
                }
            }
        }
        return s3Client as S3Client
    }

    fun setConfigInfoS3(config: ConfigInfoAWS) {
        this.config = config
    }

}