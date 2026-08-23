package no.bekk.demoapp.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/protected")
class ProtectedController {

  @GetMapping("/hello")
  fun protectedHello(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<Map<String, Any?>> =
    ResponseEntity.ok(
      mapOf(
        "message" to "Hello from protected endpoint!",
        "subject" to jwt.subject,
        "email" to jwt.getClaimAsString("email"),
        "preferred_username" to jwt.getClaimAsString("preferred_username")
      )
    )

  @GetMapping("/user-info")
  fun getUserInfo(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<Map<String, Any?>> {
    val userInfo = mutableMapOf<String, Any?>(
      "subject" to jwt.subject,
      "issuer" to jwt.issuer.toString(),
      "email" to jwt.getClaimAsString("email"),
      "emailVerified" to jwt.getClaimAsBoolean("email_verified"),
      "name" to jwt.getClaimAsString("name"),
      "preferredUsername" to jwt.getClaimAsString("preferred_username"),
      "locale" to jwt.getClaimAsString("locale"),
      "issuedAt" to jwt.issuedAt,
      "expiresAt" to jwt.expiresAt
    )

    jwt.getClaim<Any>("urn:zitadel:iam:org:project:roles")?.let { roles ->
      userInfo["roles"] = roles
    }

    return ResponseEntity.ok(userInfo)
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  fun adminOnly(): ResponseEntity<Map<String, String>> =
    ResponseEntity.ok(
      mapOf(
        "message" to "Hello Admin!",
        "access" to "This endpoint requires ADMIN role"
      )
    )

  @GetMapping("/manager")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  fun managerAccess(): ResponseEntity<Map<String, String>> =
    ResponseEntity.ok(
      mapOf(
        "message" to "Hello Manager or Admin!",
        "access" to "This endpoint requires ADMIN or MANAGER role"
      )
    )
}
