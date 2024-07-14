package autogear.frontapi.service

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.Publication
import autogear.frontapi.payload.NewPublicationPayload
import autogear.frontapi.repository.PublicationRepository
import autogear.frontapi.storage.StorageProvider
import autogear.frontapi.storage.StorageProviderFactory
import autogear.frontapi.storage.StorageType
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class PublicationService(
    private val s3Service: S3Service,
    private val autoGearConfiguration: AutoGearConfiguration,
    private val publicationRepository: PublicationRepository
): IPublicationService {

    private var storageProcess: StorageProvider? = null

    @PostConstruct
    fun init() {
        storageProcess = StorageProviderFactory.createStorageProvider(StorageType.S3, s3Service = s3Service, autoGearConfiguration.cfConfiguration.bucketName )
    }

    override fun getPublications() {
        TODO("Not yet implemented")
    }

    override fun getPublicationById() {
        TODO("Not yet implemented")
    }

    override fun createNewPublication(newPublicationPayload: NewPublicationPayload, coverImage: MultipartFile) {
        val coverImageKey = UUID.randomUUID().toString()
        storageProcess?.store(coverImageKey, coverImage)

        val publication = Publication(
            type = newPublicationPayload.type,
            title = newPublicationPayload.title,
            description = newPublicationPayload.description,
            author = newPublicationPayload.author,
            publisher = newPublicationPayload.publisher,
            genres = newPublicationPayload.genres,
            status = newPublicationPayload.status,
            releaseYear = newPublicationPayload.releaseYear,
            artist =  newPublicationPayload.artist)
        publicationRepository.save(publication.copy(coverImageKey = coverImageKey))
    }
}