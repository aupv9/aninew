package autogear.frontapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant


@Document(collection = "social_media_providers")
data class SocialMediaProvider(
    @Id
    override var id: String? = null,

    val name: String,  // e.g., "Facebook", "Google"

    val clientId: String,

    val clientSecret: String,

    val redirectUrl: String,

    val createdAt: Instant = Instant.now()
): AbstractEntity()
