package no.bekk.demoapp.config

import jakarta.websocket.Session
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

  @Bean
  open fun securityFilterChain(http: HttpSecurity, jwtAuthenticationConverter: JwtAuthenticationConverter): SecurityFilterChain {
    http
      .csrf { it.disable() }
      .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests {  auth ->
        auth
          .requestMatchers("/api/public/**").permitAll()
          .requestMatchers("/actuator/heath").permitAll()
          .anyRequest().authenticated()
      }
      .oauth2ResourceServer { oauth2 ->
        oauth2.jwt() { jwt ->
          jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
        }
      }
    return http.build()
  }

  @Bean
  fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
    val grantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter().apply {
      setAuthoritiesClaimName("urn:zitadel:iam:org:project:roles")
      setAuthorityPrefix("ROLE_")
    }

    return JwtAuthenticationConverter().apply {
      setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)
    }
  }
}
