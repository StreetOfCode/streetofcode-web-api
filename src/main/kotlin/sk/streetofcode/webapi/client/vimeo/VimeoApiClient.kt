package sk.streetofcode.webapi.client.vimeo

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class VimeoApiClient(
    @Qualifier("vimeoRestTemplate") private val restTemplate: RestTemplate,
) {

    fun getVideoDurationInSeconds(videoId: String? = null): Int {
        if (videoId.isNullOrEmpty()) {
            return 0
        }

        val response = restTemplate.getForEntity<String>("/videos/$videoId")
        return getDuration(response.body!!)
    }

    private fun getDuration(responseJson: String): Int {
        return (JSONObject(responseJson).getInt("duration"))
    }
}
