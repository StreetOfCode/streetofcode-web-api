package sk.streetofcode.webapi.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfiguration : WebMvcConfigurer {

    @Value("\${cors.allowed-origin-patterns}")
    private lateinit var allowedOriginPatterns: List<String>
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOriginPatterns(*allowedOriginPatterns.toTypedArray())
            .exposedHeaders("Content-Range")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH", "OPTIONS")
    }
}
