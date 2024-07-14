package autogear.frontapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties(prefix = "auto-gear")
class AutoGearConfiguration {
    var api: ApiConfiguration = ApiConfiguration()
    var security: SecurityConfiguration = SecurityConfiguration()
    var mail: MailConfiguration = MailConfiguration()
    var clientConfiguration : ClientConfiguration = ClientConfiguration()
    var redisConfiguration  : RedisConfiguration = RedisConfiguration()
    var cfConfiguration : CFConfiguration = CFConfiguration()
    var driveConfiguration = DriveConfiguration()
    var gcpConfiguration = GcpConfiguration()

    class DriveConfiguration {
       var name : String = "dev1"
    }

    class GcpConfiguration{
        var projectId: String = ""
        var bucketName: String = ""
    }

    class CFConfiguration {
        var region: String = ""
        var accessKey: String = ""
        var secretKey: String = ""
        var accountId: String = ""
        var bucketName: String = ""
        var endPoint: String = ""
    }

    class RedisConfiguration{
        var host: String = ""
        var port: Int = 0
        var password: String = ""
        var userName : String = ""
        var type: String = "alone"
        var cacheWithTTL: Map<String, Long> = mapOf(
            "AT" to 15, "RT" to 60 * 24
        )
    }

    class ApiConfiguration {
        var port: Int = 0
        var host: String = ""
        var version: String = "v1"
    }

    class DatabaseConfiguration {
        var url: String = ""
        var username: String = ""
        var password: String = ""
    }

    class SecurityConfiguration{
        var allowList:  List<String> = mutableListOf()
        var TTLLoggedIn: Long = 0
        var MaxAttempt:  Int = 0
        var expiryAT: Long = 60 * 60 * 24
        var expiryRT:  Long = 60 * 60 * 24 * 180
        var roleHierarchyHierarchy: String = "ROLE_ADMIN > ROLE_USER"
    }

    class MailConfiguration {
        var host: String = ""
        var port: Int = 0
        var username: String = ""
        var password: String = ""
        var from: String = ""
        var subject: String = ""
        var template: String = ""
        var enabled: Boolean = false
    }

    class ClientConfiguration{
        var urlAppClient: String = ""
        var username: String = ""
        var password: String = ""
    }

}