package sk.streetofcode.courseplatformbackend.rest

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
import sk.streetofcode.courseplatformbackend.api.ChapterService
import sk.streetofcode.courseplatformbackend.api.dto.ChapterDto
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.api.request.ChapterEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
import java.util.Optional

@RestController
@RequestMapping("chapter")
class ChapterController(val chapterService: ChapterService) {

    companion object {
        private val log = LoggerFactory.getLogger(ChapterController::class.java)
    }

    @GetMapping
    @IsAdmin
    fun getAll(@RequestParam("filter", required = false) filter: Optional<String>): ResponseEntity<List<ChapterDto>> {
        return if (filter.isPresent) {
            val chapters = try {
                val courseId = JSONObject(filter.get()).getLong("courseId")
                chapterService.getByCourseId(courseId)
            } catch (e: JSONException) {
                chapterService.getAll()
            }

            buildGetAll(chapters)
        } else {
            val chapters = chapterService.getAll()
            buildGetAll(chapters)
        }
    }

    private fun buildGetAll(chapters: List<ChapterDto>): ResponseEntity<List<ChapterDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(
            "Content-Range",
            "chapter 0-${chapters.size}/${chapters.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(chapters)
    }

    @GetMapping("{id}")
    @IsAdmin
    fun get(@PathVariable("id") id: Long): ResponseEntity<ChapterDto> {
        return ResponseEntity.ok(chapterService.get(id))
    }

    @PostMapping
    @IsAdmin
    fun add(@RequestBody chapterAddRequest: ChapterAddRequest): ResponseEntity<ChapterDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.add(chapterAddRequest))
    }

    @PutMapping("{id}")
    @IsAdmin
    fun edit(@PathVariable("id") id: Long, @RequestBody chapterEditRequest: ChapterEditRequest): ResponseEntity<ChapterDto> {
        return ResponseEntity.ok(chapterService.edit(id, chapterEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<ChapterDto> {
        return ResponseEntity.ok(chapterService.delete(id))
    }
}
