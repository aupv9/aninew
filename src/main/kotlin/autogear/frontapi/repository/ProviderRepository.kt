package autogear.frontapi.repository

import autogear.frontapi.entity.Provider
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProviderRepository: MongoRepository<Provider, String> {
}