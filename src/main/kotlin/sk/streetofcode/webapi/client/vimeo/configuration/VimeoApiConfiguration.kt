package sk.streetofcode.webapi.client.vimeo.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties
class VimeoApiConfiguration {

    @Bean
    fun vimeoRestTemplate(builder: RestTemplateBuilder, vimeoProperties: VimeoProperties): RestTemplate {
        return builder
            .rootUri("https://api.vimeo.com/")
            .defaultHeader("Authorization", "Bearer ${vimeoProperties.authToken}")
            .build()
    }
}
