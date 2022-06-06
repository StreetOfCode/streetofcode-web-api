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
import sk.streetofcode.courseplatformbackend.api.QuizQuestionAnswerService
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionAnswerDto
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionAnswerAddRequest
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionAnswerEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAuthenticated
import java.util.*

@RestController
@RequestMapping("/quiz/question")
class QuizQuestionAnswerController(val quizQuestionAnswerService: QuizQuestionAnswerService) {

    companion object {
        private val log = LoggerFactory.getLogger(QuizQuestionAnswerController::class.java)
    }

    @GetMapping("/answer")
    @IsAdmin
    fun getAll(@RequestParam("filter", required = true) filter: Optional<String>): ResponseEntity<List<QuizQuestionAnswerDto>> {
        return if (filter.isPresent) {
            val answers = try {
                val questionId = JSONObject(filter.get()).getLong("quizQuestionId")
                quizQuestionAnswerService.getAllForQuestion(questionId)
            } catch (e: JSONException) {
                log.error("Problem with parsing filter parameter, check react-admin", e)
                quizQuestionAnswerService.getAll()
            }

            buildGetAll(answers)
        } else {
            val answers = quizQuestionAnswerService.getAll()

            buildGetAll(answers)
        }
    }

    private fun buildGetAll(answers: List<QuizQuestionAnswerDto>): ResponseEntity<List<QuizQuestionAnswerDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(
            "Content-Range",
            "answer 0-${answers.size}/${answers.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(answers)
    }

    @GetMapping("/{questionId}/answer")
    @IsAuthenticated
    fun getAllForQuestion(@PathVariable("questionId") questionId: Long): ResponseEntity<List<QuizQuestionAnswerDto>> {
        return ResponseEntity.ok(quizQuestionAnswerService.getAllForQuestion(questionId))
    }

    @GetMapping("/answer/{id}")
    @IsAuthenticated
    fun get(@PathVariable("id") id: Long): ResponseEntity<QuizQuestionAnswerDto> {
        return ResponseEntity.ok(quizQuestionAnswerService.get(id))
    }

    @PostMapping("/answer")
    @IsAdmin
    fun add(@RequestBody answerAddRequest: QuizQuestionAnswerAddRequest): ResponseEntity<QuizQuestionAnswerDto> {
        val response = quizQuestionAnswerService.add(answerAddRequest)
        println(response)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/answer/{id}")
    @IsAdmin
    fun edit(@PathVariable("id") id: Long, @RequestBody answerEditRequest: QuizQuestionAnswerEditRequest): ResponseEntity<QuizQuestionAnswerDto> {
        return ResponseEntity.ok(quizQuestionAnswerService.edit(id, answerEditRequest))
    }

    @DeleteMapping("/answer/{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<QuizQuestionAnswerDto> {
        return ResponseEntity.ok(quizQuestionAnswerService.delete(id))
    }
}
