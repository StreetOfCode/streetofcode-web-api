package sk.streetofcode.webapi.service

import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.YouTubeService
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.client.youtube.YoutubeProperties

@Service
class YouTubeServiceImpl(val youtube: YouTube, val youtubeProperties: YoutubeProperties) : YouTubeService {
    companion object {
        private val log = LoggerFactory.getLogger(YouTubeServiceImpl::class.java)

        // YouTube API returns empty result if more than 50 videos are requested
        const val MAX_VIDEOS_PER_REQUEST = 50
    }

    private val videosCache: MutableMap<String, Video> = mutableMapOf()

    override fun getVideos(): List<Video> {
        if (videosCache.isNotEmpty()) {
            log.info("Returning ${videosCache.size} videos from cache.")
            return videosCache.map { it.value }
        }

        log.info("Video cache is empty, fetching videos.")

        val channelVideos = getPlaylistVideos(youtubeProperties.channelPlaylistId)
        val videosIdsToIgnore = youtubeProperties.playlistsToIgnore
            .map { playlistToIgnore -> getPlaylistVideos(playlistToIgnore).map { video -> video.id } }
            .flatten()
            .toSet()

        val videos = channelVideos.filter { !videosIdsToIgnore.contains(it.id) }

        videosCache.clear()
        log.info("Cache cleared.")

        videosCache.putAll(videos.map { it.id to it })
        log.info("${videosCache.size} videos added to cache.")

        return videos
    }

    override fun getVideo(id: String): Video {
        if (videosCache.contains(id)) {
            log.info("Returning video $id from cache.")
            return videosCache[id]!!
        }

        log.info("Video $id not found in cache, fetching.")

        val video = getVideosById(listOf(id)).firstOrNull()
            ?: throw ResourceNotFoundException("Video with id $id was not found")

        videosCache[video.id] = video

        log.info("Video $id added to cache.")

        return video
    }

    override fun clearCache() {
        log.info("Clearing videos cache.")
        videosCache.clear()
        log.info("Videos cache cleared.")
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
        playlistItemRequest.maxResults = MAX_VIDEOS_PER_REQUEST.toLong()
        playlistItemRequest.playlistId = playlistId

        var nextToken: String? = null
        do {
            playlistItemRequest.pageToken = nextToken
            val playlistItemResult = playlistItemRequest.execute()
            for (playlistItem in playlistItemResult.items) videoIdList.add(playlistItem.contentDetails.videoId)
            nextToken = playlistItemResult.nextPageToken
        } while (nextToken != null)

        val videos = videoIdList.chunked(MAX_VIDEOS_PER_REQUEST).map { chunk ->
            getVideosById(chunk)
        }.flatten()

        return videos
    }
}
