package autogear.frontapi.service

import autogear.frontapi.mapper.PublicationDTO
import autogear.frontapi.payload.NewPublicationPayload
import org.springframework.web.multipart.MultipartFile

interface IPublicationService {
    fun getPublications(): Collection<PublicationDTO>
    fun getPublicationById()
    fun createNewPublication(newPublicationPayload: NewPublicationPayload, coverImage: MultipartFile)
}