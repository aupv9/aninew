package autogear.frontapi.filter

import org.junit.jupiter.api.Test

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtAuthenticationFilterTest {

    val restTemplate: TestRestTemplate = TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_COOKIES)

    companion object{
        const val accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJleHAiOjE3MDE3NDc5OTgsImlhdCI6MTcwMTc0NzkzNywidXNlcklEIjoiNzAwODNhNDMtMmE2Yi00NDUyLThmZWEtNmFlNzcwNGM0ZjQ0IiwiZW1haWwiOiJhdXB2OTZAZ21haWwuY29tIn0.JZHbosvCXlAst1D4LJ8MU2G2tV7wAFvbnKxC3CHTMBkCwJ9bLWbMGQrdjCYkReDyv_MldBSxdeGHJ5nhIQutabmFX5CMydf0Pgpof1lt3Q5E5xM_1YgS5GAdqf8Vrn1ufoCXA21aQIv_ZGwz-BvbnOvq7mq4mGBF22ErUniudIyz-4WkCEFq3kYz6EYc8E67oCo-p0P6e13LEEiSIawd_f1SV5-18ai0rqlW-GxOY4DjrA1hA1Fr-Vrvv9pYq5i7OH98dmHpwjrAQNKoXUaoL9uX02B2NY-bBX2Zf6yceLrxzd5SkuwOOo3VB7zXkDhz5-ZMz6Y6okhgQqix_BxYdw"
        const val refreshToken = ""
    }

    @Test
    fun `doFilterInternal`() {
//        val cookies = listOf(
//            ResponseCookie.from(USER_SESSION, accessToken).httpOnly(true).secure(true)
//                .path("/").build(),
//            ResponseCookie.from(LOGGED_IN, "true").httpOnly(true).secure(true)
//                .path("/").build(),
//            ResponseCookie.from(AG_SESSION, refreshToken).httpOnly(true).secure(true)
//                .path("/").build(),
//        )
//        val headers = HttpHeaders()
//        cookies.forEach { headers.add(HttpHeaders.SET_COOKIE, it.toString()) }
//
//        val requestEntity = RequestEntity.get(URI.create("http://localhost:8001/v1/api/auth/allUser"))
//            .headers(headers)
//            .build();
//
//        val response = restTemplate.exchange(requestEntity, CommonResponse::class.java)
//
//        assertEquals(HttpStatus.OK, response.statusCode)

    }
}