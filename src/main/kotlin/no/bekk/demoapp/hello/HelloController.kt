package no.bekk.demoapp.hello

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
  @GetMapping("/", produces = ["application/json"])
  fun index() = Hello(text = "Hello World")

  data class Hello(
    val text: String,
  )
}
