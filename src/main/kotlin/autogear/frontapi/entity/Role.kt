package autogear.frontapi.entity

import autogear.frontapi.enum.RoleStage
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "roles")
data class Role(
    @Id
    override var id: String? = null,
    var name: String? = null,
    var description: String? = "",
    val stage: RoleStage = RoleStage.BETA,
    var permission: Set<Permission> = emptySet(),
    var parent: Role? = null
) : AbstractEntity()