package autogear.frontapi.service

import autogear.frontapi.configuration.AutoGearConfiguration
import autogear.frontapi.entity.*
import autogear.frontapi.mapper.*
import autogear.frontapi.payload.ChapterResponse
import autogear.frontapi.payload.MangaResponse
import autogear.frontapi.payload.NewMangaPayload
import autogear.frontapi.payload.PageResponse
import autogear.frontapi.repository.MangaRepository
import autogear.frontapi.repository.ResourceRepository
import autogear.frontapi.storage.StorageProvider
import autogear.frontapi.storage.StorageProviderFactory
import autogear.frontapi.storage.StorageType
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*


@Service
class MangaService(
    private val mangaRepository: MangaRepository,
    private val resourceService: ResourceService,
    private val mangaMapper: MangaMapper,
//    private val driveService: DriveService,
    private val autoGearConfiguration: AutoGearConfiguration,
    private val resourceMapper: ResourceMapper,
    private val chapterMapper: ChapterMapper,
    private val resourceRepository: ResourceRepository,
    private val s3Service: S3Service
): IMangaService {

    private lateinit var storageProvider: StorageProvider

    @PostConstruct
    fun init() {
        storageProvider = StorageProviderFactory.createStorageProvider(StorageType.S3, s3Service, autoGearConfiguration.cfConfiguration.bucketName)
    }


    override fun getAllManga(): List<MangaDto> {
        return mangaRepository.findAll().map {
            mangaMapper.toDto(it)
        }
    }


    override fun getManga() {
        TODO("Not yet implemented")
    }

    override fun getMangaById(id: String): MangaResponse? {
        val manga = mangaRepository.findById(id)
        if (manga.isEmpty)
            return null
        val mangaResponse = MangaResponse(manga.get().id, manga.get().metadata)
//        val chapters = manga.get().chapters.map { chapter ->
//            val chapterResponse = ChapterResponse(chapter.title, chapter.number)
//            val pages = chapter.pages?.map { page ->
//                val pageResponse = PageResponse(page.number)
//                val key = manga.get().metadata.title + "/" + chapter.title +"/"
//                pageResponse.resourceUrl = storageProvider.generatePresignedUrl(key, Duration.of(3600, ChronoUnit.SECONDS))
//                pageResponse
//            }
//            chapterResponse.pages = pages
//            chapterResponse
//        }
//        mangaResponse.chapters = chapters
       return mangaResponse
    }

    override fun getMangaByTitle(title: String) {
        TODO("Not yet implemented")
    }

    override fun getMangaByAuthor(author: String) {
        TODO("Not yet implemented")
    }

    override fun getMangaByPublisher(publisher: String) {
        TODO("Not yet implemented")
    }

    override fun getMangaByGenre(genre: String) {
        TODO("Not yet implemented")
    }

    override fun getMangaByStatus(status: String) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRating(rating: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRatingGreaterThan(rating: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRatingLessThan(rating: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRatingBetween(start: Double, end: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRatingNot(rating: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRatingGreaterThanNot(rating: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRatingLessThanNot(rating: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByRatingBetweenNot(start: Double, end: Double) {
        TODO("Not yet implemented")
    }

    override fun getMangaByTags(tags: List<String>) {
        TODO("Not yet implemented")
    }

    private fun newResource(key: String, nameFile: String){

        val file = FileInfo()
        file.path = storageProvider.generatePresignedUrl(key, Duration.of(3600, ChronoUnit.SECONDS))
        file.uploadDate = Date()
        file.fileName = nameFile

        val resourceNew = Resource(provider = ProviderType.CLOUDFLARE, file = file)

        var objectResponse = storageProvider.retrieve(key)

        if (objectResponse != null){
            objectResponse = objectResponse  as GetObjectResponse
            file.contentType = objectResponse.contentType()
            file.size = objectResponse.contentLength().toLong()
        }
        resourceRepository.save(resourceNew)

    }
    override fun createManga(payload: NewMangaPayload) {
        val mangaEntity = Manga(metadata = payload.metadata)
        val bucketName = autoGearConfiguration.cfConfiguration.bucketName

        storageProvider.store(mangaEntity.metadata.title.plus("/"), null)

        newResource(mangaEntity.metadata.title, mangaEntity.metadata.title)

        mangaRepository.save(mangaEntity)
    }

    override fun updateManga(manga: MangaDto) {
    }

    override fun deleteManga(id: Int) {
        TODO("Not yet implemented")
    }

    override fun updateMangaChapter(mangaId: String, chapterForManga: ChapterForManga) {
//        val manga = this.mangaRepository.findById(mangaId)
//        val bucketName = autoGearConfiguration.cfConfiguration.bucketName
//
//        if (manga.isPresent){
//            val mangaEntity = manga.get()
//            if(mangaEntity.chapters.isNotEmpty()){
//                for (chapter in chapterForManga.chapters!!){
//                    val chapterItem = mangaEntity.chapters.firstOrNull { it.number == chapter.number && it.title == chapter.title }
//                    if (chapterItem != null){
//                        val keyOfChapter = mangaEntity.metadata.title + "/" + chapterItem.title +"/"
//                        storageProvider.store(keyOfChapter, null)
//
//                        newResource(keyOfChapter, chapterItem.title)
//
//                        chapterItem.title = chapter.title
//                        chapterItem.number = chapter.number
//                    }else{
//                        val keyOfChapter = mangaEntity.metadata.title + "/" +  chapter.title +"/"
//                        storageProvider.store(keyOfChapter, null)
//
//                        newResource(keyOfChapter,  chapter.title)
//
//                        val newChapter = Chapter(
//                            chapter.title, number = chapter.number,
//                        )
//                        mangaEntity.chapters = mangaEntity.chapters.plus(newChapter)
//                    }
//                }
//            }else{
//                mangaEntity.chapters = chapterForManga.chapters!!.map {
//                    val keyOfChapter = mangaEntity.metadata.title + "/" +  it.title +"/"
//                    storageProvider.store(keyOfChapter, null)
//
//                    newResource(keyOfChapter, it.title)
//                    val chapter = Chapter(
//                        it.title, number = it.number,
//                    )
//                    chapter
//                }
//            }
//            this.mangaRepository.save(mangaEntity)
//        }else{
//            throw Exception("Manga not found")
//        }
    }

    override fun updatePageChapter(mangaId: String, title: String, number: Int, image: MultipartFile?, pageChapterManga: PageChapterManga) {
//        val manga = this.mangaRepository.findById(mangaId)
//        if (manga.isPresent){
//            val mangaEntity = manga.get()
//            if(mangaEntity.chapters.isNotEmpty()){
//                val chapterItem = mangaEntity.chapters.firstOrNull { it.title == title && it.number == number }
//                if (chapterItem != null){
//                    if(chapterItem.pages!!.isNotEmpty()){
//                        val page = chapterItem.pages?.firstOrNull { it.number == pageChapterManga.page }
//                        if (page != null){
//                            if(image != null){
//                                val key = mangaEntity.metadata.title + "/" + chapterItem.title + "/" + page.number
//                                storageProvider.store(key, image)
//                                newResource(key, image.name)
//                            }
//                            page.number = pageChapterManga.page
//                        }else{
//                            if(image != null){
//                               val key = mangaEntity.metadata.title + "/" + chapterItem.title + "/" + pageChapterManga.page
//                                storageProvider.store(key, image)
//                                newResource(key, image.name)
//                            }
//                            val newPage = Page(
//                                number = pageChapterManga.page
//                            )
//                            chapterItem.pages = chapterItem.pages!!.plus(newPage)
//                        }
//                    }else{
//                        if(image != null){
//                            val key = mangaEntity.metadata.title + "/" + chapterItem.title + "/" + pageChapterManga.page
//                            storageProvider.store(key, image)
//                            newResource(key, image.name)
//                        }
//                        val newPage = Page(
//                            pageChapterManga.page,
//                        )
//                        chapterItem.pages = listOf(newPage)
//                    }
//                }
//            }
//            this.mangaRepository.save(mangaEntity)
//        }else{
//            throw Exception("Manga not found")
//        }
    }
}