package autogear.frontapi.service

import autogear.frontapi.payload.NewPublicationPayload
import org.springframework.web.multipart.MultipartFile

interface IPublicationService {
    fun getPublications()
    fun getPublicationById()
    fun createNewPublication(newPublicationPayload: NewPublicationPayload, coverImage: MultipartFile)
}