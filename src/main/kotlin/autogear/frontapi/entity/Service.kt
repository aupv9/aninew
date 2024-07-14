package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "services")
class Service(
    @Id
    override var id: String? = null,
    var name: String,
    var permission: Set<Permission> = setOf()
): AbstractEntity()