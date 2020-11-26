package sk.streetofcode.courseplatformbackend.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationService {
    companion object {
        const val ADMIN_GROUP_NAME = "admin"
    }

    fun isAdmin(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication.authorities

        return authorities.contains(ADMIN_GROUP_NAME.toAuthority())
    }

    private fun String.toAuthority() = SimpleGrantedAuthority(toRole())

    private fun String.toRole() = "ROLE_$this"
}
