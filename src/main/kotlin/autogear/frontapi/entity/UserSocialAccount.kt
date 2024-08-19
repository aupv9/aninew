package autogear.frontapi.entity

import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

data class UserSocialAccount(
    @Field("provider_name")
    val providerName: String,

    @Field("social_media_user_id")
    val socialMediaUserId: String,

    val accessToken: String,

    val refreshToken: String? = null,

    val tokenExpiry: Instant? = null,

    val linkedAt: Instant = Instant.now()
)
