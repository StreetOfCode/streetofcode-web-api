package sk.streetofcode.webapi.integration

import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionDto
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.QuizQuestionAddRequest
import sk.streetofcode.webapi.api.request.QuizQuestionEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation
import sk.streetofcode.webapi.model.quiz.QuizQuestionType

@SpringBootTestAnnotation
class QuizQuestionIntegrationTests : IntegrationTests() {
    init {
        "get questions" {
            val questionsResponse = getQuestions()
            questionsResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = questionsResponse.headers["Content-Range"]
            contentRange shouldBe listOf("quizQuestion 0-3/3")

            val questions = questionsResponse.body!!
            questions.size shouldBe 3
        }

        "add question" {
            val newQuestion = QuizQuestionAddRequest(
                1,
                -1,
                "text",
                QuizQuestionType.SINGLE_CHOICE
            )

            val question = addQuestion(newQuestion)
            question.quiz.id shouldBe 1
            question.questionOrder shouldBe -1
            question.text shouldBe "text"
            question.type shouldBe QuizQuestionType.SINGLE_CHOICE
        }

        "add question invalid quizId" {
            val notFoundQuestion = QuizQuestionAddRequest(
                -1,
                -999,
                "text",
                QuizQuestionType.MULTIPLE_CHOICE
            )

            addQuestionInvalidQuizId(notFoundQuestion)
        }

        "edit question" {
            val invalidQuestionRequest = QuizQuestionEditRequest(
                1,
                -5,
                99,
                "editedText",
                QuizQuestionType.MULTIPLE_CHOICE
            )

            editQuestionInvalid(-1, invalidQuestionRequest)
            editQuestionInvalid(1, invalidQuestionRequest)

            val editRequest = QuizQuestionEditRequest(
                1,
                1,
                999,
                "editedText",
                QuizQuestionType.MULTIPLE_CHOICE
            )

            val editedQuestion = editQuestion(1, editRequest)
            editedQuestion.quiz.id shouldBe 1
            editedQuestion.questionOrder shouldBe 999
            editedQuestion.text shouldBe "editedText"
            editedQuestion.type shouldBe QuizQuestionType.MULTIPLE_CHOICE
        }

        "delete question" {
            val question = addQuestion(
                QuizQuestionAddRequest(
                    1,
                    555,
                    "newQuestion",
                    QuizQuestionType.SINGLE_CHOICE
                )
            )

            val removedQuestion = deleteQuestion(question.id)
            question.shouldBeEqualToIgnoringFields(removedQuestion, QuizQuestionDto::quiz)
            question.quiz.id shouldBe removedQuestion.quiz.id

            getQuestionNotFound(question.id)
        }
    }

    private fun getQuestionNotFound(id: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/quiz/question/$id").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun deleteQuestion(id: Long): QuizQuestionDto {
        return restWithAdminRole().deleteForEntity<QuizQuestionDto>("/quiz/question/$id").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getQuestions(): ResponseEntity<List<QuizQuestionDto>> {
        return restWithAdminRole().getForEntity("/quiz/question")
    }

    private fun addQuestion(body: QuizQuestionAddRequest): QuizQuestionDto {
        return restWithAdminRole().postForEntity<QuizQuestionDto>("/quiz/question", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun addQuestionInvalidQuizId(body: QuizQuestionAddRequest) {
        restWithAdminRole().postForEntity<ResourceNotFoundException>("/quiz/question", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editQuestionInvalid(id: Long, body: QuizQuestionEditRequest) {
        restWithAdminRole().putForEntity<Exception>("/quiz/question/$id", body).let {
            if (id != body.id) {
                it.statusCode shouldBe HttpStatus.BAD_REQUEST
            } else {
                it.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }
    }

    private fun editQuestion(id: Long, body: QuizQuestionEditRequest): QuizQuestionDto {
        return restWithAdminRole().putForEntity<QuizQuestionDto>("/quiz/question/$id", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
