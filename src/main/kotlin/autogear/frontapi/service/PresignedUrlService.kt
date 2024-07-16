package autogear.frontapi.service

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.PresignedUrlInfo
import autogear.frontapi.repository.PresignedUrlRepository
import autogear.frontapi.storage.StorageProvider
import autogear.frontapi.storage.StorageProviderFactory
import autogear.frontapi.storage.StorageType
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class PresignedUrlService(
    private val presignedUrlRepository: PresignedUrlRepository,
    private val s3Service: S3Service,
    private val autoGearConfiguration: AutoGearConfiguration,
) {
    private var storageProcess: StorageProvider? = null

    @PostConstruct
    fun init() {
        storageProcess = StorageProviderFactory.createStorageProvider(StorageType.S3, s3Service = s3Service, autoGearConfiguration.cfConfiguration.bucketName )
    }

    fun getPresignedUrl(objectKey: String, expirationMinutes: Long): String{
        val now = Instant.now()
        return presignedUrlRepository.findByObjectKeyAndExpiresAtAfter(objectKey, now).map {
            it.url
        }.orElseGet {
            generateAndCachePresignedUrl(objectKey, expirationMinutes)
        }
    }

    private fun generateAndCachePresignedUrl(objectKey: String, expirationMinutes: Long): String{
        val expiresAt = Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)
        val newUrl = storageProcess?.generatePresignedUrl(objectKey, Duration.of(expirationMinutes, ChronoUnit.MINUTES))
        val presignedUrlInfo = PresignedUrlInfo(
            objectKey = objectKey, url = newUrl!!, expiresAt = expiresAt, createdAt = Instant.now()
        )
        presignedUrlRepository.save(presignedUrlInfo)
        return newUrl
    }
}