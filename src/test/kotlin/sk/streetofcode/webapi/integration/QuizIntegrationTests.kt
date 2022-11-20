package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.webapi.api.dto.quiz.QuizDto
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.QuizAddRequest
import sk.streetofcode.webapi.api.request.QuizEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class QuizIntegrationTests : IntegrationTests() {
    init {
        "add quiz" {
            val newQuiz = QuizAddRequest(
                2,
                "testTitle",
                "testSubtitle",
                "testFinishedMessage"
            )

            val quiz = addQuiz(newQuiz)

            val fetchedQuiz = getQuiz(quiz.id)
            fetchedQuiz.lectureId shouldBe 2
            fetchedQuiz.title shouldBe "testTitle"
            fetchedQuiz.subtitle shouldBe "testSubtitle"
            fetchedQuiz.finishedMessage shouldBe "testFinishedMessage"

            val quizzesResponse = getQuizzes()
            quizzesResponse.find { it.id == quiz.id } shouldBe quiz
        }

        "add quiz invalid lectureId" {
            val newQuiz = QuizAddRequest(
                -1,
                "testTitle",
                "testSubtitle",
                "testFinishedMessage"
            )

            addQuizInvalidLectureId(newQuiz)
        }

        "edit quiz" {
            val invalidQuizRequest = QuizEditRequest(
                1,
                -1,
                "testTitleEdited",
                "testSubtitleEdited",
                "testFinishedMessageEdited"
            )

            editQuizInvalid(-1, invalidQuizRequest)
            editQuizInvalid(1, invalidQuizRequest)

            val editRequest = QuizEditRequest(
                1,
                2,
                "testTitleEdited",
                "testSubtitleEdited",
                "testFinishedMessageEdited"
            )

            val editedQuiz = editQuiz(1, editRequest)
            editedQuiz.lectureId shouldBe 2
            editedQuiz.title shouldBe "testTitleEdited"
            editedQuiz.subtitle shouldBe "testSubtitleEdited"
            editedQuiz.finishedMessage shouldBe "testFinishedMessageEdited"
        }

        "delete quiz" {
            val quiz = addQuiz(QuizAddRequest(3, "title", "subtitle", "finishedMessage"))

            val removedQuiz = deleteQuiz(quiz.id)
            removedQuiz shouldBe quiz

            getQuizNotFound(quiz.id)
        }
    }

    private fun addQuizInvalidLectureId(body: QuizAddRequest) {
        return restWithAdminRole().postForEntity<ResourceNotFoundException>("/quiz", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun getQuizzes(): List<QuizDto> {
        return restWithAdminRole().getForEntity<Array<QuizDto>>("/quiz").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    private fun addQuiz(body: QuizAddRequest): QuizDto {
        return restWithAdminRole().postForEntity<QuizDto>("/quiz", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun getQuiz(id: Long): QuizDto {
        return restWithUserRole().getForEntity<QuizDto>("/quiz/$id").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getQuizNotFound(id: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/quiz/$id").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editQuizInvalid(id: Long, body: QuizEditRequest) {
        return restWithAdminRole().putForEntity<Exception>("/quiz/$id", body).let {
            if (id != body.id) {
                it.statusCode shouldBe HttpStatus.BAD_REQUEST
            } else {
                it.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }
    }

    private fun editQuiz(id: Long, body: QuizEditRequest): QuizDto {
        return restWithAdminRole().putForEntity<QuizDto>("/quiz/$id", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteQuiz(id: Long): QuizDto {
        return restWithAdminRole().deleteForEntity<QuizDto>("/quiz/$id").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
