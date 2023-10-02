package sk.streetofcode.webapi.api

import com.google.api.services.youtube.model.Video

interface YouTubeService {
    fun getVideos(): List<Video>
    fun getVideo(id: String): Video
    fun clearCache()
}
