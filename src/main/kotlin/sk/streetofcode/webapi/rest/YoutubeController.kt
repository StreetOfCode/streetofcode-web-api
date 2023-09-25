package sk.streetofcode.webapi.rest

import com.google.api.services.youtube.model.Video
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.webapi.api.YouTubeService

@RestController
@RequestMapping("youtube")
class YoutubeController(val youTubeService: YouTubeService) {
    @GetMapping
    fun getAll(): ResponseEntity<List<Video>> {
        return ResponseEntity.ok(youTubeService.getVideos())
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: String): ResponseEntity<Video> {
        return ResponseEntity.ok(youTubeService.getVideo(id))
    }
}
