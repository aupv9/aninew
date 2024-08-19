package autogear.frontapi.service

import autogear.frontapi.entity.Job
import autogear.frontapi.entity.Platform
import org.springframework.stereotype.Component

@Component
class SocialMediaClientFactory {

    fun createClient(platform: Platform, accessToken: String): SocialMediaClient {
        return when (platform) {
            Platform.FACEBOOK -> FacebookClient(accessToken)
            Platform.TWITTER -> TwitterClient(accessToken)
            else -> throw IllegalArgumentException("Unknown platform: $platform")
        }
    }
}

interface SocialMediaClient {
    fun post(content: Job.Payload): Boolean
}

class FacebookClient(
    private val accessToken: String
): SocialMediaClient{
    override fun post(content: Job.Payload): Boolean {
        TODO("Not yet implemented")
    }

}

class TwitterClient(
    private val accessToken: String
): SocialMediaClient{
    override fun post(content: Job.Payload): Boolean {
        TODO("Not yet implemented")
    }

}