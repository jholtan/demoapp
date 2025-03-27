package no.bekk.demoapp.hello

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @GetMapping("/", produces = ["application/json"])
  fun index(): Hello {
    logger.warn("Calling index")
    return Hello(text = "Hello World")
  }

  data class Hello(
    val text: String,
  )
}
