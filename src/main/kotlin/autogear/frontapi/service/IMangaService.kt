package autogear.frontapi.service

import autogear.frontapi.mapper.ChapterForManga
import autogear.frontapi.mapper.MangaDto
import autogear.frontapi.mapper.PageChapterManga
import autogear.frontapi.payload.MangaResponse
import autogear.frontapi.payload.NewMangaPayload
import org.springframework.web.multipart.MultipartFile

interface IMangaService {
    fun getAllManga(): List<MangaDto>
    fun getManga()
    fun getMangaById(id: String): MangaResponse?
    fun getMangaByTitle(title: String)
    fun getMangaByAuthor(author: String)
    fun getMangaByPublisher(publisher: String)
    fun getMangaByGenre(genre: String)
    fun getMangaByStatus(status: String)
    fun getMangaByRating(rating: Double)
    fun getMangaByRatingGreaterThan(rating: Double)
    fun getMangaByRatingLessThan(rating: Double)
    fun getMangaByRatingBetween(start: Double, end: Double)

    fun getMangaByRatingNot(rating: Double)
    fun getMangaByRatingGreaterThanNot(rating: Double)
    fun getMangaByRatingLessThanNot(rating: Double)
    fun getMangaByRatingBetweenNot(start: Double, end: Double)

    fun getMangaByTags(tags: List<String>)

    fun createManga(payload: NewMangaPayload)
    fun updateManga(manga: MangaDto)
    fun deleteManga(id: Int)

    fun updateMangaChapter(mangaId: String, chapterForManga: ChapterForManga)

    fun updatePageChapter(
        mangaId: String,
        title: String,
        number: Int,
        image: MultipartFile?,
        pageChapterManga: PageChapterManga
    )
}