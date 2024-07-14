//package autogear.frontapi.controller
//
//import common.RequestLogin
//import org.junit.jupiter.api.Test
//
//import org.junit.jupiter.api.Assertions.*
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.boot.test.web.client.TestRestTemplate
//import org.springframework.context.annotation.Bean
//import org.springframework.http.HttpStatus
//
//@TestConfiguration
//class TestConfig {
//
//    @Bean
//    fun restTemplateBuilder(): TestRestTemplate {
//        return TestRestTemplate()
//    }
//}
//
//@SpringBootTest
//class AuthenticateControllerTest {
//
//
//    val restTemplate: TestRestTemplate = TestRestTemplate()
//
//
//    @Test
//    fun `test brute force login protection`() {
//        val username = "aupv96@gmail.com"
//        val password = "123457"
//
//        // Perform multiple login attempts
//        repeat(4) {
//            val response = restTemplate.postForEntity(
//                "http://localhost:8001/v1/api/auth/login",
//                RequestLogin(username, password),
//                String::class.java
//            )
//
//            // Assert that login fails (you might customize this based on your actual response structure)
//            assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
//        }
//
//        // Sleep to simulate a delay between login attempts
//        Thread.sleep(5000)
//
//        // One more attempt after a delay, this time it should succeed
//        val responseAfterDelay = restTemplate.postForEntity(
//            "http://localhost:8001/v1/api/auth/login",
//            RequestLogin(username, password),
//            String::class.java
//        )
//
//    }
//}