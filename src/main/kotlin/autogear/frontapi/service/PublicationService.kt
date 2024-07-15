package autogear.frontapi.service

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.Publication
import autogear.frontapi.mapper.PublicationDTO
import autogear.frontapi.mapper.PublicationMapper
import autogear.frontapi.payload.NewPublicationPayload
import autogear.frontapi.repository.PublicationRepository
import autogear.frontapi.storage.StorageProvider
import autogear.frontapi.storage.StorageProviderFactory
import autogear.frontapi.storage.StorageType
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.UUID

@Service
class PublicationService(
    private val s3Service: S3Service,
    private val autoGearConfiguration: AutoGearConfiguration,
    private val publicationRepository: PublicationRepository,
    private val publicationMapper: PublicationMapper
): IPublicationService {

    private var storageProcess: StorageProvider? = null

    @PostConstruct
    fun init() {
        storageProcess = StorageProviderFactory.createStorageProvider(StorageType.S3, s3Service = s3Service, autoGearConfiguration.cfConfiguration.bucketName )
    }

    override fun getPublications(): Collection<PublicationDTO> {
        return publicationRepository.findAll().map {
            val publicationDTO = publicationMapper.toDto(it)
            publicationDTO.coverImageURL = storageProcess?.generatePresignedUrl(it.coverImageKey!!, Duration.of(3600, ChronoUnit.MINUTES))
            publicationDTO
        }
    }

    override fun getPublicationById() {6000
        TODO("Not yet implemented")
    }

    override fun createNewPublication(newPublicationPayload: NewPublicationPayload, coverImage: MultipartFile) {
        val coverImageKey = UUID.randomUUID().toString()
        storageProcess?.store(coverImageKey, coverImage)

        val publication = Publication(
            type = newPublicationPayload.type!!,
            title = newPublicationPayload.title!!,
            description = newPublicationPayload.description!!,
            author = newPublicationPayload.author!!,
            publisher = newPublicationPayload.publisher!!,
            genres = newPublicationPayload.genres!!,
            status = newPublicationPayload.status!!,
            releaseYear = newPublicationPayload.releaseYear,
            artist =  newPublicationPayload.artist)
        publicationRepository.save(publication.copy(coverImageKey = coverImageKey))
    }
}