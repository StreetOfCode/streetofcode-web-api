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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.QuizQuestionService
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionDto
import sk.streetofcode.webapi.api.request.QuizQuestionAddRequest
import sk.streetofcode.webapi.api.request.QuizQuestionEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAdmin
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import java.util.Optional

@RestController
class QuizQuestionController(val quizQuestionService: QuizQuestionService) {

    companion object {
        private val log = LoggerFactory.getLogger(QuizController::class.java)
    }

    @GetMapping("/quiz/question")
    @IsAdmin
    fun getAll(
        @RequestParam(
            "filter",
            required = false
        ) filter: Optional<String>
    ): ResponseEntity<List<QuizQuestionDto>> {
        return if (filter.isPresent) {
            val questions = try {
                val quizId = JSONObject(filter.get()).getLong("quizId")
                quizQuestionService.getAllForQuiz(quizId)
            } catch (e: JSONException) {
                quizQuestionService.getAll()
            }

            buildGetAll(questions)
        } else {
            val questions = quizQuestionService.getAll()
            buildGetAll(questions)
        }
    }

    private fun buildGetAll(questions: List<QuizQuestionDto>): ResponseEntity<List<QuizQuestionDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(
            "Content-Range",
            "quizQuestion 0-${questions.size}/${questions.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(questions)
    }

    @GetMapping("/quiz/{quizId}/question")
    @IsAuthenticated
    fun getAllForQuiz(@PathVariable("quizId") quizId: Long): ResponseEntity<List<QuizQuestionDto>> {
        return ResponseEntity.ok(quizQuestionService.getAllForQuiz(quizId))
    }

    @GetMapping("/quiz/question/{id}")
    @IsAuthenticated
    fun get(@PathVariable("id") id: Long): ResponseEntity<QuizQuestionDto> {
        return ResponseEntity.ok(quizQuestionService.get(id))
    }

    @PostMapping("/quiz/question")
    @IsAdmin
    fun add(@RequestBody quizQuestionAddRequest: QuizQuestionAddRequest): ResponseEntity<QuizQuestionDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizQuestionService.add(quizQuestionAddRequest))
    }

    @PutMapping("/quiz/question/{id}")
    @IsAdmin
    fun edit(
        @PathVariable("id") id: Long,
        @RequestBody questionEditRequest: QuizQuestionEditRequest
    ): ResponseEntity<QuizQuestionDto> {
        return ResponseEntity.ok(quizQuestionService.edit(id, questionEditRequest))
    }

    @DeleteMapping("/quiz/question/{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<QuizQuestionDto> {
        return ResponseEntity.ok(quizQuestionService.delete(id))
    }
}
