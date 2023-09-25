package sk.streetofcode.webapi.service

import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.YouTubeService
import sk.streetofcode.webapi.client.youtube.YoutubeProperties

@Service
class YouTubeServiceImpl(val youtube: YouTube, val youtubeProperties: YoutubeProperties) : YouTubeService {
    companion object {
        private val log = LoggerFactory.getLogger(YouTubeServiceImpl::class.java)
    }

    private fun getVideosById(id: List<String>): List<Video> {
        val request = youtube.Videos().list(listOf("id", "snippet"))
        request.id = id
        return request.execute().items
    }

    private fun getPlaylistVideos(playlistId: String): List<Video> {
        val videoIdList: MutableList<String> = ArrayList()

        val playlistItemRequest = youtube.playlistItems().list(listOf("contentDetails"))
        playlistItemRequest.fields = "nextPageToken,items/contentDetails/videoId"
        playlistItemRequest.maxResults = 50
        playlistItemRequest.playlistId = playlistId

        var nextToken: String? = null
        do {
            playlistItemRequest.pageToken = nextToken
            val playlistItemResult = playlistItemRequest.execute()
            for (playlistItem in playlistItemResult.items) videoIdList.add(playlistItem.contentDetails.videoId)
            nextToken = playlistItemResult.nextPageToken
        } while (nextToken != null)

        val videos = videoIdList.chunked(50).map { chunk ->
            getVideosById(chunk)
        }.flatten()

        return videos
    }

    override fun getVideos(): List<Video> {
        val channelVideos = getPlaylistVideos(youtubeProperties.channelPlaylistId)
        val videosIdsToIgnore = youtubeProperties.playlistsToIgnore
            .map { playlistToIgnore -> getPlaylistVideos(playlistToIgnore).map { video -> video.id } }
            .flatten()
            .toSet()

        return channelVideos.filter { !videosIdsToIgnore.contains(it.id) }
    }

    override fun getVideo(id: String): Video = getVideosById(listOf(id)).first()
}
