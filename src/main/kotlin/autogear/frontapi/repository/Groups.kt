package autogear.frontapi.repository

import autogear.frontapi.entity.Group
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface GroupRepository: MongoRepository<Group, String>, PagingAndSortingRepository<Group, String> {
    fun findByParentId(groupID: String, pageRequest: PageRequest): Page<Group>

    fun findByIdAndOwnerUserID(groupID: String, owner: String): Optional<Group>

    fun findById(groupID: String, pageRequest: PageRequest): Page<Group>

    fun findByParentId(groupID: String): List<Group>

    fun findByOwnerUserID(owner: String, pageRequest: PageRequest): List<Group>

    fun countByNameEqualsAndOwnerUserID(name: String, userID: String): Int

    fun countByNameContainingIgnoreCaseAndOwnerUserID(name: String, userID: String): Int

    fun countByParentIsNullAndOwnerUserID(userID: String): Int
    fun countByOwnerUserID(userID: String): Int

}