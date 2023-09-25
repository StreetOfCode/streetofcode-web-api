package sk.streetofcode.webapi.client.youtube

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequestInitializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
class YoutubeConfiguration {

    @Bean
    fun youtube(youtubeProperties: YoutubeProperties): YouTube {
        return YouTube.Builder(NetHttpTransport(), GsonFactory(), null)
            .setYouTubeRequestInitializer(YouTubeRequestInitializer(youtubeProperties.apiKey))
            .build()
    }
}

@ConfigurationProperties("youtube")
class YoutubeProperties {
    lateinit var apiKey: String
    lateinit var channelPlaylistId: String
    lateinit var playlistsToIgnore: List<String>
}
