package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "users")
open class User(
    @Id override var id: String? = null,
    var name: String? = null,
    var email: String,
    var password: String? = null,
    var userID: String,
    var roles: List<Role>? = emptyList(),
    var enabled: Boolean? = false,
    var accountNonExpired: Boolean? = null,
    var credentialsNonExpired: Boolean? = null,
    var accountNonLocked: Boolean? = null,
    var status: String? = null,
    @Field("social_accounts")
    val socialAccounts: MutableList<UserSocialAccount> = mutableListOf()
) : AbstractEntity()


