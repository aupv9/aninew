package autogear.frontapi.service

import autogear.frontapi.mapper.PublicationDTO
import autogear.frontapi.payload.Chapter
import autogear.frontapi.payload.ChapterDetail
import autogear.frontapi.payload.NewPublicationPayload
import org.springframework.web.multipart.MultipartFile

interface IPublicationService {
    fun getPublications(): Collection<PublicationDTO>
    fun getPublicationById(id: String): PublicationDTO
    fun createNewPublication(newPublicationPayload: NewPublicationPayload, coverImage: MultipartFile)

    fun updatePublication(id: String, publicationPayload: NewPublicationPayload, coverImage: MultipartFile?)

    fun addChapterForPublication(id: String,chapterPublication: Chapter)

    fun getInfoChapterPub(id: String): Collection<ChapterDetail>?
}