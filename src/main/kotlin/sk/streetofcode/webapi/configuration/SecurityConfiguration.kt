package sk.streetofcode.webapi.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.firewall.HttpStatusRequestRejectedHandler
import org.springframework.security.web.firewall.RequestRejectedHandler

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!test")
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Value("\${streetofcode.enable-mock-auth:false}")
    private var enableMockAuth: String = "false"

    companion object {
        const val AUTHORITY_PREFIX = "ROLE_"
        const val AUTHORITIES_CLAIM_NAME = "roles"
    }

    @Bean
    fun requestRejectedHandler(): RequestRejectedHandler? {
        // Get rid of too many logs of
        // RequestRejectedException: The request was rejected because the URL contained a potentially malicious String "//"
        return HttpStatusRequestRejectedHandler()
    }

    override fun configure(http: HttpSecurity) {
        if (enableMockAuth == "true") {
            http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll()
        } else {
            http.csrf().disable()
                .oauth2ResourceServer { oauth2 ->
                    oauth2.jwt().jwtAuthenticationConverter(FirebaseAuthenticationConverter())
                }
        }

        // To enable H2
        http.headers().frameOptions().disable()
    }
}

/**
 * Enables Spring to use Firebase roles as Spring authorities (roles).
 */
class FirebaseAuthenticationConverter() : JwtAuthenticationConverter() {
    init {
        setJwtGrantedAuthoritiesConverter(getFirebaseAuthoritiesConverter())
    }

    private fun getFirebaseAuthoritiesConverter(): Converter<Jwt?, MutableCollection<GrantedAuthority?>?>? {
        val converter = JwtGrantedAuthoritiesConverter()
        converter.setAuthorityPrefix(SecurityConfiguration.AUTHORITY_PREFIX)
        converter.setAuthoritiesClaimName(SecurityConfiguration.AUTHORITIES_CLAIM_NAME)
        return converter
    }
}
