package sk.streetofcode.webapi.client.convertkit.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties
class ConvertKitApiConfiguration {

    @Bean
    fun convertKitRestTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.rootUri("https://api.convertkit.com/v3").build()
    }
}
