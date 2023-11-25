package sk.streetofcode.webapi.client.wp.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties
class WpApiConfiguration {
    @Bean
    fun wpGraphqlRestTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.rootUri("https://wp.streetofcode.sk").build()
    }
}
