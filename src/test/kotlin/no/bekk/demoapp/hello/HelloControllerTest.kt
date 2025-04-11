package no.bekk.demoapp.hello

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HelloControllerTest(@Autowired val restTemplate: TestRestTemplate) {
  @Test
  fun `Assert content and status code`() {
    val entity = restTemplate.getForEntity<String>("/")
    assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(entity.body).contains("Hello World")
  }
}
