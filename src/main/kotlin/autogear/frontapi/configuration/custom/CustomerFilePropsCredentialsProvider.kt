package autogear.frontapi.configuration.custom

import autogear.frontapi.configuration.AutoGearConfiguration
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider

@Component
class CustomerFilePropsCredentialsProvider(
    private val autoGearConfiguration: AutoGearConfiguration
): AwsCredentialsProvider {

    override fun resolveCredentials(): AwsCredentials {
        return AwsBasicCredentials.create(
            autoGearConfiguration.cfConfiguration.accessKey,
            autoGearConfiguration.cfConfiguration.secretKey
        )
    }
}
