package autogear.frontapi.repository

import autogear.frontapi.entity.Execution
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExecutionRepository: MongoRepository<Execution, String> {
}