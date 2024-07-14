package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "presignedUrlInfo")
data class PresignedUrlInfo(
    @Id
    override var id: String? = null,
    @Field("object_key")
    val objectKey: String,
    @Field("url")
    val url: String,
    @Field("expiration_time")
    val expirationTime: Long
): AbstractEntity()
