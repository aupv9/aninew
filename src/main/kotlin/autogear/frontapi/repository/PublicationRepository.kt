package autogear.frontapi.repository

import autogear.frontapi.entity.Publication
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PublicationRepository: MongoRepository<Publication, String> {
}