package sk.streetofcode.courseplatformbackend.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "swagger")
data class SwaggerProperties(
    var authorizeUri: String = "",
    var tokenUri: String = "",
    var clientId: String = "",
    var clientSecret: String = ""
)
