package autogear.frontapi.controller

import autogear.frontapi.payload.NewPublicationPayload
import autogear.frontapi.service.IPublicationService
import autogear.frontapi.utils.JsonUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/publications")
class PublicationController(
    private val publicationService: IPublicationService
) {


    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun createPublication(@RequestPart("body") newPublicationPayload: String,
                          @RequestPart("file") file: MultipartFile): ResponseEntity<Any>{
        val publication = JsonUtils.objectMapper.readValue(newPublicationPayload, NewPublicationPayload::class.java)
        publicationService.createNewPublication(publication, file)
        return ResponseEntity.noContent().build()
    }
}