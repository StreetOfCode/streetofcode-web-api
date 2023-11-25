package sk.streetofcode.webapi.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.webapi.api.WPService

@RestController
@RequestMapping("wp")
class WPController(val wpService: WPService) {
    @GetMapping("post/{slug}")
    fun getPostBySlug(
        @PathVariable("slug") slug: String,
        @RequestParam("revalidate") revalidate: Boolean
    ): ResponseEntity<String> {
        return ResponseEntity.ok(wpService.getPostBySlug(slug, revalidate))
    }
}
