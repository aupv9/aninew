package autogear.frontapi.repository

import autogear.frontapi.entity.Manga
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MangaRepository: MongoRepository<Manga, String> {
}