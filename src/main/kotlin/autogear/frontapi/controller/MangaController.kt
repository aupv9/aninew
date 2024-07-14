package autogear.frontapi.controller

import autogear.frontapi.mapper.ChapterForManga
import autogear.frontapi.mapper.MangaDto
import autogear.frontapi.mapper.PageChapterManga
import autogear.frontapi.payload.MangaResponse
import autogear.frontapi.payload.NewMangaPayload
import autogear.frontapi.service.IMangaService
import autogear.frontapi.utils.JsonUtils
import common.CommonResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/mangas")
class MangaController(
    private val mangaService: IMangaService
) {


    @PostMapping
    fun createManga(@RequestBody payload: NewMangaPayload): ResponseEntity<CommonResponse<Any>> {
        this.mangaService.createManga(payload)
        return ResponseEntity.noContent().build()
    }

    @PutMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE])
    fun updateManga(@RequestParam("mangaId") mangaId: String, @RequestBody mangaDto: MangaDto):  ResponseEntity<CommonResponse<Any>> {
        this.mangaService.getMangaById(mangaId) ?: return ResponseEntity.notFound().build()
        this.mangaService.updateManga(mangaDto)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getMangas(): ResponseEntity<CommonResponse<List<MangaDto>>> {
        return ResponseEntity.ok(CommonResponse(this.mangaService.getAllManga()))
    }

    @GetMapping("/id")
    fun getMangaById(@RequestParam("mangaId") mangaId: String): ResponseEntity<CommonResponse<MangaResponse>> {
        return ResponseEntity.ok(CommonResponse(this.mangaService.getMangaById(mangaId)))
    }

    @PutMapping("/chapter")
    fun updateMangaChapter(@RequestParam("mangaId") mangaId: String,
                           @RequestBody chapterForManga: ChapterForManga
                           ): ResponseEntity<CommonResponse<Any>> {
        return ResponseEntity.ok(
            CommonResponse(
                this.mangaService.updateMangaChapter(
                    mangaId,
                    chapterForManga)
        ))
    }

    @PutMapping("/chapter/page", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun updatePageChapter(
        @RequestPart("image", required = false) image: MultipartFile,
        @RequestPart("body") pageChapterManga: String,
        @RequestParam("mangaId") mangaId: String,
        @RequestParam("chapterTitle") chapterTitle: String,
        @RequestParam("numberChapter") numberChapter: Int
    ): ResponseEntity<CommonResponse<Any>> {
        return ResponseEntity.ok(
            CommonResponse(
                this.mangaService.updatePageChapter(
                    mangaId,
                    chapterTitle,
                    numberChapter,
                    image, JsonUtils.objectMapper.readValue(pageChapterManga, PageChapterManga::class.java))
        ))
    }

}