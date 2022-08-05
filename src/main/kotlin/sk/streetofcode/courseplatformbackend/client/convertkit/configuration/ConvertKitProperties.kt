package sk.streetofcode.courseplatformbackend.client.convertkit.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("convertkit")
class ConvertKitProperties {
    lateinit var apiKey: String
    lateinit var formId: String
}
