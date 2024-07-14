package autogear.frontapi.repository

import autogear.frontapi.entity.Resource
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ResourceRepository: MongoRepository<Resource, String> {
    fun findByFileFileName(providerId: String): Optional<Resource>

    fun findByFileProviderId(providerId: String): Optional<Resource>

}