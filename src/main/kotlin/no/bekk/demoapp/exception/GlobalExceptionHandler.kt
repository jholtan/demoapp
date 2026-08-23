package no.bekk.demoapp.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.time.Clock

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException::class)
  fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<Map<String, Any>> =
    ResponseEntity.status(HttpStatus.FORBIDDEN).body(
      mapOf(
        "error" to "Access Denied",
        "message" to "You do not have permission to access this resource",
        "timestamp" to Clock.System.now().toString()
      )
    )

  @ExceptionHandler(JwtException::class)
  fun handleJwtException(ex: JwtException): ResponseEntity<Map<String, Any>> =
    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
      mapOf(
        "error" to "Invalid Token",
        "message" to (ex.message ?: "Token validation failed"),
        "timestamp" to Clock.System.now().toString()
      )
    )

  @ExceptionHandler(Exception::class)
  fun handleGenericException(ex: JwtException): ResponseEntity<Map<String, Any>> =
    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      mapOf(
        "error" to "Internal Server Error",
        "message" to (ex.message ?: "An unexpected error occurred"),
        "timestamp" to Clock.System.now().toString()
      )
    )

}
