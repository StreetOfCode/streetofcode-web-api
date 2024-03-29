package sk.streetofcode.webapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import sk.streetofcode.webapi.api.exception.InvalidAuthenticationException
import sk.streetofcode.webapi.configuration.SecurityConfiguration.Companion.AUTHORITY_PREFIX

@Component
class AuthenticationService {
    companion object {
        const val ADMIN_GROUP_NAME = "admin"
        const val USER_GROUP_NAME = "user"
        private const val SUB_CLAIM_NAME = "sub"
    }

    @Value("\${streetofcode.enable-mock-auth:false}")
    private var enableMockAuth: String = "false"

    fun isAdmin(): Boolean {
        if (enableMockAuth == "true") return true

        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication.authorities

        return authorities.contains(ADMIN_GROUP_NAME.toAuthority())
    }

    fun isUser(): Boolean {
        if (enableMockAuth == "true") return true

        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication.authorities

        return authorities.contains(USER_GROUP_NAME.toAuthority())
    }

    fun isAuthenticated(): Boolean {
        if (enableMockAuth == "true") return true

        return SecurityContextHolder.getContext().authentication !is AnonymousAuthenticationToken
    }

    private fun String.toAuthority() = SimpleGrantedAuthority(toRole())

    private fun String.toRole() = "${AUTHORITY_PREFIX}$this"

    fun getNullableUserId(): String? {
        if (isAuthenticated()) {
            return getUserId()
        }

        return null
    }

    fun getUserId(): String {
        if (enableMockAuth == "true") return "00000000-0000-0000-0000-000000000000"

        val authentication = SecurityContextHolder.getContext().authentication
        assert(authentication is AnonymousAuthenticationToken)

        if (authentication.credentials !is Jwt) {
            throw InvalidAuthenticationException("")
        }

        val principal = authentication.credentials as Jwt
        val claims = principal.claims
        if (!claims.containsKey(SUB_CLAIM_NAME)) {
            throw InvalidAuthenticationException("")
        }

        return claims[SUB_CLAIM_NAME] as String
    }
}
