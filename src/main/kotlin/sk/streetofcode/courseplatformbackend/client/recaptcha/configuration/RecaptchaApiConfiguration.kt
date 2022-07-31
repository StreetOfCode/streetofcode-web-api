package sk.streetofcode.courseplatformbackend.client.recaptcha.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties
class RecaptchaApiConfiguration {

    @Bean
    fun recaptchaRestTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.rootUri("https://www.google.com/recaptcha/api").build()
    }
}
