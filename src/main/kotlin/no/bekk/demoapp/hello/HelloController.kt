package no.bekk.demoapp.hello

import dev.openfeature.sdk.MutableContext
import dev.openfeature.sdk.OpenFeatureAPI
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(val featureToggles: OpenFeatureAPI) {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @GetMapping("/", produces = ["application/json"])
  fun index(@RequestParam("user") userId: String?): Hello {
    logger.warn("Calling index")
    val userContext =
            MutableContext(UUID.randomUUID().toString())
                    .add("userId", userId ?: "1")
                    .add("admin", true)

    return if (featureToggles.client.getBooleanValue("message", false, userContext)) {
      Hello(text = "Hello World, but Feature Toggled")
    } else {
      Hello(text = "Hello World")
    }
  }

  @GetMapping("/user", produces = ["application/json"])
  fun getUser(@RequestParam("user") userId: String?): Hello {
    logger.warn("Calling getUser")
    HttpRequest.newBuilder()
            .uri(URI("https://localhost:8080/${userId}"))
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build()

    return Hello(text = "Hello User ${userId ?: "1"}")
  }

  data class Hello(
          val text: String,
  )
}
