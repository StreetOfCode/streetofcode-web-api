package sk.streetofcode.webapi.client.vimeo.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vimeo")
class VimeoProperties {
    lateinit var authToken: String
}
