package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.LectureService
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Lecture

@RestController
@RequestMapping("lecture")
class LectureController(val lectureService: LectureService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<Lecture>> {
        return ResponseEntity.ok(lectureService.getAll())
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Lecture> {
        return ResponseEntity.ok(lectureService.get(id))
    }

    @PostMapping
    fun add(@RequestBody lectureAddRequest: LectureAddRequest): ResponseEntity<Long> {
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureService.add(lectureAddRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Void> {
        lectureService.delete(id)
        return ResponseEntity.ok().build()
    }
}