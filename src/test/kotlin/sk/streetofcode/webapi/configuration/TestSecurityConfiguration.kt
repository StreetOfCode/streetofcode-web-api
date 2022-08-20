package sk.streetofcode.webapi.configuration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

@TestConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("test")
class TestSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser(User("admin", "{noop}admin", listOf(SimpleGrantedAuthority("ROLE_admin"))))
            .withUser(User("user", "{noop}user", listOf(SimpleGrantedAuthority("ROLE_user"))))
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().httpBasic()
    }
}
