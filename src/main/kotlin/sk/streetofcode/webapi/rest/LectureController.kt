package sk.streetofcode.webapi.rest

import org.json.JSONException
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.LectureOrderSort
import sk.streetofcode.webapi.api.LectureService
import sk.streetofcode.webapi.api.dto.LectureDto
import sk.streetofcode.webapi.api.request.LectureAddRequest
import sk.streetofcode.webapi.api.request.LectureEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAdmin
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import java.util.Optional

@RestController
@RequestMapping("lecture")
class LectureController(val lectureService: LectureService) {

    companion object {
        private val log = LoggerFactory.getLogger(LectureController::class.java)
    }

    @GetMapping
    @IsAdmin
    fun getAll(@RequestParam("filter", required = false) filter: Optional<String>, @RequestParam("sort", required = false) sort: Optional<String>): ResponseEntity<List<LectureDto>> {
        var lectureOrderSort: LectureOrderSort = LectureOrderSort.ASC
        if (sort.isPresent && sort.get().contains("lectureOrder")) {
            if (sort.get().contains("DESC")) {
                lectureOrderSort = LectureOrderSort.DESC
            }
        }

        return if (filter.isPresent) {
            val lectures = try {
                val chapterId = JSONObject(filter.get()).getLong("chapterId")
                lectureService.getByChapterId(chapterId, lectureOrderSort)
            } catch (e: JSONException) {
                lectureService.getAll(lectureOrderSort)
            }

            buildGetAll(lectures)
        } else {
            val lectures = lectureService.getAll(lectureOrderSort)
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
    @IsAuthenticated
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
