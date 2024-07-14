//package autogear.frontapi.configuration
//
//import autogear.frontapi.configuration.DriveConfiguration.Companion.CREDENTIALS_FILE_PATH
//import com.google.auth.Credentials
//import com.google.auth.oauth2.GoogleCredentials
//import org.springframework.context.annotation.Bean
//import com.google.cloud.storage.*;
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
//import org.springframework.context.ApplicationContext
//import org.springframework.context.annotation.Configuration
//
//@Configuration
//class GoogleCloudStorageConfiguration{
//
//    @Autowired
//    lateinit var applicationContext: ApplicationContext
//
//    @Autowired
//    lateinit var autoGearConfiguration: AutoGearConfiguration
//
//    @Bean
//    fun getCredentials(): Credentials{
//        val inputStream = applicationContext.getResource("classpath:".plus(CREDENTIALS_FILE_PATH)).inputStream
//
//        val credentials = GoogleCredentials.fromStream(inputStream)
//
//        credentials.refreshIfExpired()
//
//        return credentials
//    }
//
//    @Bean
//    @ConditionalOnBean(Credentials::class)
//    fun getStorage(): Storage?{
//        return StorageOptions.newBuilder().setCredentials(getCredentials())
//            .setProjectId(autoGearConfiguration.gcpConfiguration.projectId)
//            .build().service
//    }
//}