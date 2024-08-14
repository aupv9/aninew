package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user_socials")
data class UserSocial(
    @Id
    override var id: String? = null,
    var userId: String,
    var attributes: Map<String, String>? = emptyMap()
): AbstractEntity()
