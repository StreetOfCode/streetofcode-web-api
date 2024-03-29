package sk.streetofcode.webapi.rest

import com.google.api.services.youtube.model.Video
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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

    @GetMapping("clear-cache")
    fun clearCache(): ResponseEntity<String> {
        youTubeService.clearCache()
        return ResponseEntity.ok("Cache cleared")
    }
}
