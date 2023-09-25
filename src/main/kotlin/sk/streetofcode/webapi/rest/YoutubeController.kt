package sk.streetofcode.webapi.rest

import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoListResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.webapi.client.youtube.YoutubeProperties
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated


@RestController
@RequestMapping("youtube")
class YoutubeController(val youtube: YouTube, val youtubeProperties: YoutubeProperties) {

    @GetMapping
    @IsAuthenticated
    fun get(): ResponseEntity<List<Video>> {
        val videoIdList: MutableList<String> = ArrayList()

        val playlistItemRequest = youtube.playlistItems().list(listOf("contentDetails"))
        playlistItemRequest.setFields("nextPageToken,items/contentDetails/videoId")
        playlistItemRequest.setMaxResults(50)
        playlistItemRequest.setPlaylistId(youtubeProperties.playlistId)

        var nextToken = ""
        do {
            playlistItemRequest.setPageToken(nextToken)
            val playlistItemResult = playlistItemRequest.execute()
            for (playlistItem in playlistItemResult.items) videoIdList.add(playlistItem.contentDetails.videoId)
            nextToken = playlistItemResult.nextPageToken
        } while (nextToken != null)

        val videoRequest = youtube.Videos().list(listOf("id", "snippet"))
        videoRequest.setId(videoIdList)
        val videoList: VideoListResponse = videoRequest.execute()
        return ResponseEntity.ok(videoList.items)
    }
}
