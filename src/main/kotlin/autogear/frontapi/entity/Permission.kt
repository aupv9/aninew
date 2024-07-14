package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "permission")
data class Permission(
    @Id
    override var id: String? = null,
    val name: String,
    val description: String? =  null,
): AbstractEntity()