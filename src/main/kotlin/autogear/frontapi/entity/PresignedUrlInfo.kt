package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(collection = "presigned_url_info")
data class PresignedUrlInfo(
    @Id
    override var id: String? = null,
    @Field("object_key")
    val objectKey: String,
    @Field("url")
    var url: String,
    @Field("expires_at")
    @Indexed
    var expiresAt: Instant,
    @Field("created_at")
    var createdAt: Instant
): AbstractEntity()
