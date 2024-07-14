package autogear.frontapi.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "keys")
data class Keys(
    var user: String,
    var publicKey: String,
    var refreshToken: List<String>,
    var accessToken: String ? = null,
    var type: TypeKey? =  TypeKey.REGISTER
): AbstractEntity()

enum class TypeKey{
    ACCESS, REFRESH, REGISTER, RESET
}