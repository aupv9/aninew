package autogear.frontapi.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "provider")
data class Provider(
    val name: String,
    val providerId: String
): AbstractEntity()
