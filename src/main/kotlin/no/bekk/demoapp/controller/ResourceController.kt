package no.bekk.demoapp.controller

import no.bekk.demoapp.model.Resource
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/api/resource")
class ResourceController {

  private val resources = ConcurrentHashMap<Long, Resource>()
  private val idGenerator = AtomicLong(1)

  @GetMapping
  fun getAllResources(): ResponseEntity<List<Resource>> =
    ResponseEntity.ok(resources.values.toList())

  @GetMapping("/{id}")
  fun getResource(@PathVariable id: Long): ResponseEntity<Resource> {
    val resource = resources[id]
      ?: return ResponseEntity.notFound().build()
    return ResponseEntity.ok(resource)
  }

  @PostMapping
  fun createResource(
    @RequestBody resource: Resource,
    @AuthenticationPrincipal jwt: Jwt
  ): ResponseEntity<Resource> {
    val id = idGenerator.getAndIncrement()
    val newResource = resource.copy(
      id = id,
      createdBy = jwt.subject
    )
    resources[id] = newResource
    return ResponseEntity.ok(newResource)
  }

  @PutMapping("/{id}")
  fun updateResource(
    @PathVariable id: Long,
    @RequestBody resource: Resource,
    @AuthenticationPrincipal jwt: Jwt
  ): ResponseEntity<Resource> {
    if (!resources.containsKey(id)) {
      return ResponseEntity.notFound().build()
    }

    val updatedResource = resource.copy(id = id).apply {
      markUpdated(jwt.subject)
    }
    resources[id] = updatedResource

    return ResponseEntity.ok(updatedResource)
  }

  @DeleteMapping("/{id}")
  fun deleteResource(@PathVariable id: Long): ResponseEntity<Void> {
    return if (resources.remove(id) != null) {
      ResponseEntity.noContent().build()
    } else {
      ResponseEntity.notFound().build()
    }
  }
}
