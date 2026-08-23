package no.bekk.demoapp.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class ZitadelJwtAuthenticationConverter: Converter<Jwt, AbstractAuthenticationToken> {

  companion object {
    private const val ZITADEL_ROLES_CLAIM = "urn:zitadel:iam:org:project:roles"
  }

  override fun convert(jwt: Jwt): AbstractAuthenticationToken {
    val authorities = extractAuthorities(jwt)
    return JwtAuthenticationToken(jwt, authorities)
  }

  private fun extractPrincipalName(jwt: Jwt): String {
    return jwt.getClaimAsString("preferred_username") ?: jwt.subject
  }

  private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
    val authorities = mutableListOf<GrantedAuthority>()

    jwt.getClaim<Map<String, Any>>(ZITADEL_ROLES_CLAIM)?.let { rolesClaim ->
      rolesClaim.keys.forEach { role ->
        authorities.add(SimpleGrantedAuthority("ROLE_${role.uppercase()}"))
      }
    }

    jwt.getClaim<List<String>>("roles")?.forEach { role ->
      authorities.add(SimpleGrantedAuthority("ROLE_${role.uppercase()}"))
    }

    authorities.add(SimpleGrantedAuthority("ROLE_USER"))
    return authorities
  }
}
