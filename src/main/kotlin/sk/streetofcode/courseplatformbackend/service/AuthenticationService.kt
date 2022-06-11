package sk.streetofcode.courseplatformbackend.service

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.exception.InvalidAuthenticationException

@Component
class AuthenticationService {
    companion object {
        const val ADMIN_GROUP_NAME = "admin"
        const val USER_GROUP_NAME = "user"
        private const val SUB_CLAIM_NAME = "sub"
    }

    fun isAdmin(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication.authorities

        return authorities.contains(ADMIN_GROUP_NAME.toAuthority())
    }

    fun isUser(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication.authorities

        return authorities.contains(USER_GROUP_NAME.toAuthority())
    }

    fun isAuthenticated() = SecurityContextHolder.getContext().authentication !is AnonymousAuthenticationToken

    private fun String.toAuthority() = SimpleGrantedAuthority(toRole())

    private fun String.toRole() = "ROLE_$this"

    fun getUserId(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        assert(authentication is AnonymousAuthenticationToken)

        val principal = authentication.credentials as Jwt
        val claims = principal.claims
        if (!claims.containsKey(SUB_CLAIM_NAME)) {
            throw InvalidAuthenticationException("")
        }

        return claims[SUB_CLAIM_NAME] as String
    }
}
