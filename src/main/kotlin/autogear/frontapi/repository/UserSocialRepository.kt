package autogear.frontapi.repository

import autogear.frontapi.entity.UserSocial
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSocialRepository: MongoRepository<UserSocial, String> {
    fun findByUserId(userId: String): Optional<UserSocial>
}