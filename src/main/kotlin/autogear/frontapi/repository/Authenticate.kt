package autogear.frontapi.repository

import autogear.frontapi.entity.*
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository: MongoRepository<User, String>{
    fun findByEmail(email:String): Optional<User>
    fun findByUserID(userID: String): Optional<User>
    fun findByRolesNameIn(roles: List<String?>): List<User>

}

@Repository
interface RoleRepository : MongoRepository<Role, String> {
    fun findByName(name: String): Role?
}

@Repository
interface VerifyTokenRepo:  MongoRepository<VerificationToken, String>{
    fun findByToken(token: String): Optional<VerificationToken>

    fun findByUserUserID(userID: String): Optional<VerificationToken>
}

@Repository
interface KeysRepository: MongoRepository<Keys, String> {
    fun findByUser(user: String): Optional<Keys>
    fun findByUserAndType(userID: String, type: TypeKey): Optional<Keys>
}

@Repository
interface PrivilegeRepo: MongoRepository<Permission, String>{
    fun findByName(name: String): Permission?
}