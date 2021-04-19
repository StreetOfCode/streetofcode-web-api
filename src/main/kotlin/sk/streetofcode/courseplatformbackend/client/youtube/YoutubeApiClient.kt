package sk.streetofcode.courseplatformbackend.client.youtube

import org.json.JSONException
import org.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.client.youtube.configuration.YoutubeProperties
import java.time.Duration

@Service
class YoutubeApiClient(private val restTemplate: RestTemplate, private val youtubeProperties: YoutubeProperties) {

    fun getVideoDurationInSeconds(videoEmbedUrl: String? = null):Int {
        if (videoEmbedUrl == null) {
            return 0
        }

        val response = restTemplate.getForEntity<String>("/videos?id=${getIdFromEmbedUrl(videoEmbedUrl)}&part=contentDetails&key=${youtubeProperties.apiKey}")
        return youtubeDurationToSeconds(getDurationFromResponse(videoEmbedUrl, response.body!!))
    }

    /***
     * i.e if videoEmbedUrl is https://www.youtube.com/embed/abcd then function will return abcd
     */
    private fun getIdFromEmbedUrl(videoEmbedUrl: String): String {
        return videoEmbedUrl.substringAfterLast("/")
    }

    /***
     * i.e request - https://www.googleapis.com/youtube/v3/videos?id=8kHpMCDCbUA&part=contentDetails&key=AIzaSyBSMZaSm7yaHSr4q0zCkO0DBxAnI6YJSXc
     * YouTube API - https://developers.google.com/youtube/v3/docs/videos#resource-representation
     */
    private fun getDurationFromResponse(videoEmbedUrl: String, responseJsonString: String): String {
        return try {
            (JSONObject(responseJsonString).getJSONArray("items")[0] as JSONObject).getJSONObject("contentDetails").getString("duration")
        } catch (e: JSONException) {
            throw InternalErrorException("Could not parse videoUrl: $videoEmbedUrl  Response from YouTube API: $responseJsonString")
        }
    }

    /***
     * i.e: duration from YouTube response - PT1H58M30S (ISO 8601)
     */
    private fun youtubeDurationToSeconds(duration: String): Int {
        return Duration.parse(duration).toSeconds().toInt()
    }
}