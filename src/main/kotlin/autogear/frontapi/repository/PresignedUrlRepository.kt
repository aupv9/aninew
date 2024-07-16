package autogear.frontapi.repository

import autogear.frontapi.entity.PresignedUrlInfo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface PresignedUrlRepository: MongoRepository<PresignedUrlInfo, String> {
    fun findByObjectKeyAndExpiresAtAfter(objectKey: String,  now: Instant): Optional<PresignedUrlInfo>
    fun deleteByExpiresAtBefore( now: Instant)
}