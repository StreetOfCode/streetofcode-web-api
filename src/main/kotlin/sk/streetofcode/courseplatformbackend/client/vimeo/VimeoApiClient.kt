package sk.streetofcode.courseplatformbackend.client.vimeo

import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import sk.streetofcode.courseplatformbackend.client.vimeo.configuration.VimeoProperties

@Service
class VimeoApiClient(
    @Qualifier("vimeoRestTemplate") private val restTemplate: RestTemplate,
    private val properties: VimeoProperties
) {

    companion object {
        private val log = LoggerFactory.getLogger(VimeoApiClient::class.java)
    }

    fun getVideoDurationInSeconds(videoId: String? = null): Int {
        if (videoId == null) {
            return 0
        }

        val response = restTemplate.getForEntity<String>("/videos/$videoId")
        return getDuration(response.body!!)
    }

    private fun getDuration(responseJson: String): Int {
        return (JSONObject(responseJson).getInt("duration"))
    }
}
