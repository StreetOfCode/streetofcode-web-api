package sk.streetofcode.webapi.rest

import org.json.JSONException
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.webapi.api.QuizQuestionUserAnswerService
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionAnswerCorrectnessDto
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionUserAnswerDto
import sk.streetofcode.webapi.api.request.QuizQuestionUserAnswerRequest
import sk.streetofcode.webapi.configuration.annotation.IsAdmin
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import java.util.*

@RestController
class QuizQuestionUserAnswerController(val quizQuestionUserAnswerService: QuizQuestionUserAnswerService) {
    companion object {
        private val log = LoggerFactory.getLogger(QuizQuestionUserAnswerController::class.java)
    }

    @GetMapping("/quiz/question/user-answer")
    @IsAdmin
    fun getAll(@RequestParam("filter", required = true) filter: Optional<String>): ResponseEntity<List<QuizQuestionUserAnswerDto>> {
        return if (filter.isPresent) {
            val answers = try {
                val questionId = JSONObject(filter.get()).getLong("quizQuestionId")
                quizQuestionUserAnswerService.getAllAnswersForQuestion(questionId)
            } catch (e: JSONException) {
                quizQuestionUserAnswerService.getAll()
            }

            buildGetAll(answers)
        } else {
            val answers = quizQuestionUserAnswerService.getAll()

            buildGetAll(answers)
        }
    }

    private fun buildGetAll(answers: List<QuizQuestionUserAnswerDto>): ResponseEntity<List<QuizQuestionUserAnswerDto>> {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(
            "Content-Range",
            "userAnswer 0-${answers.size}/${answers.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(answers)
    }

    @GetMapping("/quiz/{quizId}/question/user-answer")
    @IsAuthenticated
    fun getAllForQuiz(@PathVariable("quizId") quizId: Long): ResponseEntity<List<QuizQuestionUserAnswerDto>> {
        return ResponseEntity.ok(quizQuestionUserAnswerService.getAllUserAnswersForQuiz(quizId))
    }

    @PostMapping("/quiz/question/user-answer")
    fun postQuizQuestionUserAnswer(@RequestBody answerRequest: QuizQuestionUserAnswerRequest): ResponseEntity<QuizQuestionAnswerCorrectnessDto> {
        return ResponseEntity.ok(quizQuestionUserAnswerService.answer(answerRequest))
    }
}
