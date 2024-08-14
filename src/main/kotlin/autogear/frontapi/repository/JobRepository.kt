package autogear.frontapi.repository

import autogear.frontapi.entity.Job
import autogear.frontapi.entity.Status
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface JobRepository: MongoRepository<Job, String> {
    fun findByStatus(status: Status): List<Job>
    fun findByUserId(userId: String): List<Job>
}