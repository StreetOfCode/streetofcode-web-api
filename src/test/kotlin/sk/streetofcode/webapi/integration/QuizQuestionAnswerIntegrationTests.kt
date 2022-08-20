package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionAnswerDto
import sk.streetofcode.webapi.api.request.QuizQuestionAnswerAddRequest
import sk.streetofcode.webapi.api.request.QuizQuestionAnswerEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class QuizQuestionAnswerIntegrationTests : IntegrationTests() {
    init {
        "get answers" {
            val answersResponse = getAnswers()
            answersResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = answersResponse.headers["Content-Range"]
            contentRange shouldBe listOf("answer 0-12/12")

            val answers = answersResponse.body!!
            answers.size shouldBe 12
        }

        "add answer" {
            val newAnswer = QuizQuestionAnswerAddRequest(
                1, "answer", false
            )

            val answer = addAnswer(newAnswer)
            answer.questionId shouldBe newAnswer.questionId
            answer.text shouldBe newAnswer.text
        }

        "add answer invalid" {
            val notFoundQuestionAnswer = QuizQuestionAnswerAddRequest(
                -1, "invalid questionId", true
            )
            addAnswerInvalid(notFoundQuestionAnswer)

            val conflictQuestionAnswer = QuizQuestionAnswerAddRequest(
                1,
                "single choice question with 2 correct answers",
                true
            )
            addAnswerInvalid(conflictQuestionAnswer, HttpStatus.CONFLICT)
        }

        "edit answer" {
            val notFoundAnswerRequest = QuizQuestionAnswerEditRequest(
                -1,
                1,
                "doesn't matter",
                false
            )

            editAnswerInvalid(1, notFoundAnswerRequest, HttpStatus.BAD_REQUEST)
            editAnswerInvalid(-1, notFoundAnswerRequest, HttpStatus.NOT_FOUND)

            val duplicateCorrectAnswer = QuizQuestionAnswerEditRequest(
                1,
                1,
                "editedText",
                true
            )

            editAnswerInvalid(1, duplicateCorrectAnswer, HttpStatus.CONFLICT)

            val editRequest = QuizQuestionAnswerEditRequest(
                1,
                2,
                "editedText",
                false
            )

            val editedAnswer = editAnswer(1, editRequest)
            editedAnswer.text shouldBe "editedText"
            editedAnswer.questionId shouldBe 2
        }

        "delete answer" {
            val answer = addAnswer(
                QuizQuestionAnswerAddRequest(
                    1,
                    "newAnswer",
                    false
                )
            )

            val removedAnswer = deleteAnswer(answer.id)
            removedAnswer shouldBe answer
        }
    }

    private fun getAnswers(): ResponseEntity<List<QuizQuestionAnswerDto>> {
        return restWithAdminRole().getForEntity("/quiz/question/answer")
    }

    private fun addAnswer(body: QuizQuestionAnswerAddRequest): QuizQuestionAnswerDto {
        return restWithAdminRole().postForEntity<QuizQuestionAnswerDto>("/quiz/question/answer", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun addAnswerInvalid(body: QuizQuestionAnswerAddRequest, expects: HttpStatus = HttpStatus.NOT_FOUND) {
        restWithAdminRole().postForEntity<Exception>("/quiz/question/answer", body).let {
            it.statusCode shouldBe expects
        }
    }

    private fun editAnswerInvalid(id: Long, body: QuizQuestionAnswerEditRequest, expects: HttpStatus = HttpStatus.NOT_FOUND) {
        restWithAdminRole().putForEntity<Exception>("/quiz/question/answer/$id", body).let {
            it.statusCode shouldBe expects
        }
    }

    private fun editAnswer(id: Long, body: QuizQuestionAnswerEditRequest): QuizQuestionAnswerDto {
        return restWithAdminRole().putForEntity<QuizQuestionAnswerDto>("/quiz/question/answer/$id", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteAnswer(id: Long): QuizQuestionAnswerDto {
        return restWithAdminRole().deleteForEntity<QuizQuestionAnswerDto>("/quiz/question/answer/$id").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
