package sk.streetofcode.courseplatformbackend.client.youtube.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("youtube")
class YoutubeProperties {
    lateinit var apiKey: String
}
