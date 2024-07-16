package autogear.frontapi.service

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.ChapterPub
import autogear.frontapi.entity.Publication
import autogear.frontapi.exception.NotFoundException
import autogear.frontapi.mapper.PublicationDTO
import autogear.frontapi.mapper.PublicationMapper
import autogear.frontapi.payload.Chapter
import autogear.frontapi.payload.NewPublicationPayload
import autogear.frontapi.repository.PublicationRepository
import autogear.frontapi.storage.StorageProvider
import autogear.frontapi.storage.StorageProviderFactory
import autogear.frontapi.storage.StorageType
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class PublicationService(
    private val s3Service: S3Service,
    private val autoGearConfiguration: AutoGearConfiguration,
    private val publicationRepository: PublicationRepository,
    private val publicationMapper: PublicationMapper,
    private val presignedUrlService: PresignedUrlService
): IPublicationService {

    private var storageProcess: StorageProvider? = null

    @PostConstruct
    fun init() {
        storageProcess = StorageProviderFactory.createStorageProvider(StorageType.S3, s3Service = s3Service, autoGearConfiguration.cfConfiguration.bucketName )
    }

    override fun getPublications(): Collection<PublicationDTO> {
        return publicationRepository.findAll().map {
            val publicationDTO = publicationMapper.toDto(it)
            publicationDTO.coverImageURL = presignedUrlService.getPresignedUrl(it.coverImageKey!!, 3600)
            publicationDTO
        }
    }

    override fun getPublicationById(id: String): PublicationDTO {
        return publicationRepository.findById(id).map { publicationMapper.toDto(it) }.orElseGet{
            throw NotFoundException("getPublicationById not found")
        }
    }

    override fun createNewPublication(newPublicationPayload: NewPublicationPayload, coverImage: MultipartFile) {
        val coverImageKey = UUID.randomUUID().toString()
        storageProcess?.store(coverImageKey, coverImage.bytes)

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

    override fun updatePublication(id: String, publicationPayload: NewPublicationPayload, coverImage: MultipartFile?) {

        val publication = Publication(
            id = id,
            type = publicationPayload.type!!,
            title = publicationPayload.title!!,
            description = publicationPayload.description!!,
            author = publicationPayload.author!!,
            publisher = publicationPayload.publisher!!,
            genres = publicationPayload.genres!!,
            status = publicationPayload.status!!,
            releaseYear = publicationPayload.releaseYear,
            artist =  publicationPayload.artist)

        if(coverImage != null){
            val coverImageKey = UUID.randomUUID().toString()
            storageProcess?.store(coverImageKey, coverImage.bytes)
            publication.coverImageKey = coverImageKey
        }
        publicationRepository.save(publication)
    }

    override fun addChapterForPublication(id: String, chapterPublication: Chapter) {
        val publication = publicationRepository.findById(id).get()

        val chapters = publication.chapters

        val chapterEntity = ChapterPub(
            number = chapterPublication.number!!,
            title = chapterPublication.title!!,
            volumeNumber = chapterPublication.volumeNumber!!,
            uploadDate = Date()
        )

        for (pageResource in chapterPublication.pageResource!!){
            val pageResourceKey = UUID.randomUUID().toString()
            storageProcess?.store(pageResourceKey, pageResource.bytes)
            chapterEntity.pageKeys = chapterEntity.pageKeys?.plus(pageResourceKey)
        }
        chapterEntity.pageCount = chapterEntity.pageKeys?.size

        chapters?.plus(chapterEntity)


        publicationRepository.save(publication)

    }
}