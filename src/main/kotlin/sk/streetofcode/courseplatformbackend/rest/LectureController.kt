package sk.streetofcode.courseplatformbackend.rest

import org.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.LectureService
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureEditRequest
import sk.streetofcode.courseplatformbackend.model.Lecture
import java.util.*

@RestController
@RequestMapping("lecture")
class LectureController(val lectureService: LectureService) {

    @GetMapping
    fun getAll(@RequestParam("filter") filter: Optional<String>): ResponseEntity<List<Lecture>> {
        return if (filter.isPresent) {
            val chapterId = JSONObject(filter.get()).getLong("chapterId")
            val lectures = lectureService.getByChapterId(chapterId)
            buildGetAll(lectures)
        } else {
            val lectures = lectureService.getAll()
            buildGetAll(lectures)
        }
    }

    private fun buildGetAll(lectures: List<Lecture>): ResponseEntity<List<Lecture>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(
                "Content-Range",
                "lecture 0-${lectures.size}/${lectures.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(lectures)
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Lecture> {
        return ResponseEntity.ok(lectureService.get(id))
    }

    @PostMapping
    fun add(@RequestBody lectureAddRequest: LectureAddRequest): ResponseEntity<Lecture> {
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureService.add(lectureAddRequest))
    }

    @PutMapping("{id}")
    fun edit(@PathVariable("id") id: Long, @RequestBody lectureEditRequest: LectureEditRequest): ResponseEntity<Lecture> {
        return ResponseEntity.ok(lectureService.edit(id, lectureEditRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Lecture> {
        return ResponseEntity.ok(lectureService.delete(id))
    }
}