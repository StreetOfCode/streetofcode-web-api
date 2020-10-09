package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.ChapterService
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.model.Chapter

@RestController
@RequestMapping("chapter")
class ChapterController(val chapterService: ChapterService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<Chapter>> {
        return ResponseEntity.ok(chapterService.getAll())
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Chapter> {
        return ResponseEntity.ok(chapterService.get(id))
    }

    @PostMapping
    fun add(@RequestBody chapterAddRequest: ChapterAddRequest): ResponseEntity<Long> {
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.add(chapterAddRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Void> {
        chapterService.delete(id)
        return ResponseEntity.ok().build()
    }
}