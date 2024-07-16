package autogear.frontapi.controller

import autogear.frontapi.payload.Chapter
import autogear.frontapi.payload.NewPublicationPayload
import autogear.frontapi.service.IPublicationService
import autogear.frontapi.utils.JsonUtils
import common.CommonResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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


    @GetMapping
    fun getPublication(): ResponseEntity<Any>{
        return ResponseEntity.ok(
            CommonResponse(
                data = publicationService.getPublications()
            )
        )
    }

    @PutMapping("/{id}",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun updatePublication(@PathVariable("id") id: String,
                          @RequestPart("body") publicationPayload: String,
                          @RequestPart("file", required = false) file: MultipartFile): ResponseEntity<Any>{
        val publicationOpt = publicationService.getPublicationById(id)
        val publication = JsonUtils.objectMapper.readValue(publicationPayload, NewPublicationPayload::class.java)
        publicationService.updatePublication(id, publication, file)
        return ResponseEntity.noContent().build()
    }


    @PutMapping("{id}/chapters",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun updateChapterForPublication(
        @RequestPart("files") filePages: List<MultipartFile>,
        @PathVariable("id") id: String,
        @RequestPart("") chapterPublication: String
    ): ResponseEntity<Any>{
        val publication = JsonUtils.objectMapper.readValue(chapterPublication, Chapter::class.java)
        publication.pageResource = filePages
        publicationService.addChapterForPublication(id, publication)
        return ResponseEntity.noContent().build()
    }
}