//package autogear.frontapi.configuration
//
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.json.gson.GsonFactory
//import com.google.api.services.drive.Drive
//import com.google.api.services.drive.DriveScopes
//import org.springframework.context.ApplicationContext
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials
//import com.google.auth.oauth2.ServiceAccountCredentials
//
//@Configuration
//class DriveConfiguration(
//    private var applicationContext: ApplicationContext,
//    private var autoGearConfiguration: AutoGearConfiguration
//) {
//    companion object {
//        const val APPLICATION_NAME = "AutoGear"
//        const val CLIENT_SECRET_FILE = "client_secret.json"
//        private const val TOKEN_DIRECTORY = "drive"
//        const val CREDENTIALS_FILE_PATH = "$TOKEN_DIRECTORY/credentials.json"
//        val JSON_FACTORY: GsonFactory = GsonFactory.getDefaultInstance()
//        val HTTP_TRANSPORT: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
//
//    }
//
//
//    @Bean
//    fun getGoogleCredentials(): GoogleCredentials {
//        val inputStream = applicationContext.getResource("classpath:".plus(CREDENTIALS_FILE_PATH)).inputStream
//        val credentials = ServiceAccountCredentials.fromStream(inputStream).createScoped(DriveScopes.all())
//        credentials.refreshIfExpired()
//        return credentials
//    }
//
//    @Bean
//    fun getDriveService(credentials: GoogleCredentials): Drive {
//        val requestInitializer = HttpCredentialsAdapter(credentials)
//        val service = Drive.Builder( NetHttpTransport(),
//            GsonFactory.getDefaultInstance(),
//            requestInitializer)
//            .build()
//        return service
//    }
//
//
//}