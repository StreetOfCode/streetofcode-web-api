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
import sk.streetofcode.courseplatformbackend.api.QuizService
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizDto
import sk.streetofcode.courseplatformbackend.api.request.QuizAddRequest
import sk.streetofcode.courseplatformbackend.api.request.QuizEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAuthenticated
import java.util.*

@RestController
@RequestMapping("quiz")
class QuizController(val quizService: QuizService) {

    companion object {
        private val log = LoggerFactory.getLogger(QuizController::class.java)
    }

    @GetMapping
    @IsAdmin
    fun getAll(@RequestParam("filter", required = false) filter: Optional<String>): ResponseEntity<List<QuizDto>> {
        return if (filter.isPresent) {
            val quizzes = try {
                val lectureId = JSONObject(filter.get()).getLong("lectureId")
                quizService.getAllForLecture(lectureId)
            } catch (e: JSONException) {
                log.error("Problem with parsing filter parameter, check react-admin", e)
                quizService.getAll()
            }

            buildGetAll(quizzes)
        } else {
            val quizzes = quizService.getAll()
            buildGetAll(quizzes)
        }
    }

    private fun buildGetAll(quizzes: List<QuizDto>): ResponseEntity<List<QuizDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(
            "Content-Range",
            "quiz 0-${quizzes.size}/${quizzes.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(quizzes)
    }

    @GetMapping("/lecture/{lectureId}")
    @IsAuthenticated
    fun getAllForLecture(@PathVariable("lectureId") lectureId: Long): ResponseEntity<List<QuizDto>> {
        return ResponseEntity.ok(quizService.getAllForLecture(lectureId))
    }

    @GetMapping("{id}")
    @IsAuthenticated
    fun get(@PathVariable("id") id: Long): ResponseEntity<QuizDto> {
        return ResponseEntity.ok(quizService.get(id))
    }

    @PostMapping
    @IsAdmin
    fun add(@RequestBody quizAddRequest: QuizAddRequest): ResponseEntity<QuizDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.add(quizAddRequest))
    }

    @PutMapping("{id}")
    @IsAdmin
    fun edit(@PathVariable("id") id: Long, @RequestBody quizEditRequest: QuizEditRequest): ResponseEntity<QuizDto> {
        return ResponseEntity.ok(quizService.edit(id, quizEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<QuizDto> {
        return ResponseEntity.ok(quizService.delete(id))
    }
}
