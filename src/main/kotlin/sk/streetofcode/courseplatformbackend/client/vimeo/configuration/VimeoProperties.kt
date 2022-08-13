package sk.streetofcode.courseplatformbackend.client.vimeo.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vimeo")
class VimeoProperties {
    lateinit var authToken: String
}
