package sk.streetofcode.webapi.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@Profile("noauth")
class SecurityNoAuthConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll()
        // To enable H2
        http.headers().frameOptions().disable()
    }
}
