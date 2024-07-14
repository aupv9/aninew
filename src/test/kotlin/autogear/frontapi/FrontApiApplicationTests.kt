package autogear.frontapi

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest
class FrontApiApplicationTests {

	val restTemplate: TestRestTemplate = TestRestTemplate()

	@Test
	fun contextLoads() {
	}

}
