package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(
    collection = "user_attribute"
)
data class UserAttribute(
    @Id override var id: String? = null,
    var name: String,
    var value: String,
    var user: User
): AbstractEntity()
