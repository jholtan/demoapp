package no.bekk.demoapp.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public")
class PublicController {

  @GetMapping("/hello")
  fun hello(): ResponseEntity<Map<String, String>> =
    ResponseEntity.ok(
      mapOf(
        "message" to "Hello from public endpoint",
        "status" to "This endpoint does not require authentication"
      )
    )

  @GetMapping("/health")
  fun health(): ResponseEntity<Map<String, String>> =
    ResponseEntity.ok(mapOf("status" to "UP"))
}
