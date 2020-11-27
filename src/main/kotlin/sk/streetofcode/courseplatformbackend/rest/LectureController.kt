package sk.streetofcode.courseplatformbackend.rest

import org.json.JSONException
import org.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.LectureService
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
import java.util.*

@RestController
@RequestMapping("lecture")
class LectureController(val lectureService: LectureService) {

    @GetMapping
    @IsAdmin
    fun getAll(@RequestParam("filter") filter: Optional<String>): ResponseEntity<List<LectureDto>> {
        return if (filter.isPresent) {
            val lectures = try {
                val chapterId = JSONObject(filter.get()).getLong("chapterId")
                lectureService.getByChapterId(chapterId)
            } catch (e: JSONException) {
                lectureService.getAll()
            }

            buildGetAll(lectures)
        } else {
            val lectures = lectureService.getAll()
            buildGetAll(lectures)
        }
    }

    private fun buildGetAll(lectures: List<LectureDto>): ResponseEntity<List<LectureDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(
                "Content-Range",
                "lecture 0-${lectures.size}/${lectures.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(lectures)
    }

    @GetMapping("{id}")
    @IsAdmin
    fun get(@PathVariable("id") id: Long): ResponseEntity<LectureDto> {
        return ResponseEntity.ok(lectureService.get(id))
    }

    @PostMapping
    @IsAdmin
    fun add(@RequestBody lectureAddRequest: LectureAddRequest): ResponseEntity<LectureDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureService.add(lectureAddRequest))
    }

    @PutMapping("{id}")
    @IsAdmin
    fun edit(@PathVariable("id") id: Long, @RequestBody lectureEditRequest: LectureEditRequest): ResponseEntity<LectureDto> {
        return ResponseEntity.ok(lectureService.edit(id, lectureEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<LectureDto> {
        return ResponseEntity.ok(lectureService.delete(id))
    }
}