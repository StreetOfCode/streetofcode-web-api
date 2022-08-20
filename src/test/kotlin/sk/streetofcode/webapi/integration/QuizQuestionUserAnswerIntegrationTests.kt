package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionAnswerCorrectnessDto
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionUserAnswerDto
import sk.streetofcode.webapi.api.request.QuizQuestionUserAnswerRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class QuizQuestionUserAnswerIntegrationTests : IntegrationTests() {
    init {
        "get user answers" {
            val answersResponse = getUserAnswers()
            answersResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = answersResponse.headers["Content-Range"]
            contentRange shouldBe listOf("userAnswer 0-1/1")

            val userAnswers = answersResponse.body!!
            userAnswers.size shouldBe 1
        }

        "get answers for quiz" {
            val answer = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(1)
                )
            )

            answer.statusCode shouldBe HttpStatus.OK
            answer.body!!.isCorrect shouldBe false

            sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(2)
                )
            )

            answer.statusCode shouldBe HttpStatus.OK
            answer.body!!.isCorrect shouldBe false

            val userAnswers = getAnswersForQuiz(1)
            userAnswers.size shouldBe 1

            val userAnswer = userAnswers.first()
            userAnswer.tryCount shouldBe 2
            userAnswer.question.id shouldBe 1
            userAnswer.answer.id shouldBe 2
        }

        "previously saved answers" {
            val multipleChoiceSingleAnswer = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    3,
                    listOf(11)
                )
            )

            multipleChoiceSingleAnswer.body?.isCorrect shouldBe false

            val singleChoiceCorrectAnswer = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(4)
                )
            )

            singleChoiceCorrectAnswer.body?.isCorrect shouldBe true
            getSavedUserAnswers(1).body?.filter { it.isCorrect }?.size shouldBe 1

            sendAnswer(
                QuizQuestionUserAnswerRequest(
                    3,
                    listOf(12, 11)
                )
            ).body?.isCorrect shouldBe true

            getSavedUserAnswers(1).body?.filter { it.isCorrect }?.size shouldBe 3
        }

        "answer question" {
            val notFoundQuestion = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    -1,
                    listOf(1)
                )
            )
            notFoundQuestion.statusCode shouldBe HttpStatus.NOT_FOUND

            val notFoundAnswer = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(-1000)
                )
            )
            notFoundAnswer.statusCode shouldBe HttpStatus.NOT_FOUND

            val incorrectAnswer = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(1)
                )
            )

            incorrectAnswer.statusCode shouldBe HttpStatus.OK
            incorrectAnswer.body!!.isCorrect shouldBe false

            val multipleAnswers = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(4, 4, 4)
                )
            )

            multipleAnswers.statusCode shouldBe HttpStatus.OK
            multipleAnswers.body!!.isCorrect shouldBe false

            val correctAnswer = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(4)
                )
            )

            correctAnswer.statusCode shouldBe HttpStatus.OK
            correctAnswer.body!!.isCorrect shouldBe true

            val multipleChoiceIncorrect = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    3,
                    listOf(9, 10)
                )
            )

            multipleChoiceIncorrect.statusCode shouldBe HttpStatus.OK
            multipleChoiceIncorrect.body!!.isCorrect shouldBe false

            val multipleChoiceCorrect = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    3,
                    listOf(11, 12)
                )
            )

            multipleChoiceCorrect.statusCode shouldBe HttpStatus.OK
            multipleChoiceCorrect.body!!.isCorrect shouldBe true

            val multipleChoiceDuplicate = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    3,
                    listOf(11, 11, 12, 12, 12, 12, 11, 11, 11)
                )
            )

            multipleChoiceDuplicate.statusCode shouldBe HttpStatus.OK
            multipleChoiceDuplicate.body!!.isCorrect shouldBe false

            val differentQuestionAnswer = sendAnswer(
                QuizQuestionUserAnswerRequest(
                    1,
                    listOf(11)
                )
            )

            differentQuestionAnswer.statusCode shouldBe HttpStatus.OK
            differentQuestionAnswer.body!!.isCorrect shouldBe false
        }
    }

    private fun getUserAnswers(): ResponseEntity<Array<QuizQuestionUserAnswerDto>> {
        return restWithAdminRole().getForEntity("/quiz/question/user-answer")
    }

    private fun getSavedUserAnswers(quizId: Long): ResponseEntity<Array<QuizQuestionUserAnswerDto>> {
        return restWithUserRole().getForEntity("/quiz/$quizId/question/user-answer")
    }

    private fun sendAnswer(body: QuizQuestionUserAnswerRequest): ResponseEntity<QuizQuestionAnswerCorrectnessDto> {
        return restWithUserRole().postForEntity("/quiz/question/user-answer", body)
    }

    private fun getAnswersForQuiz(quizId: Long): List<QuizQuestionUserAnswerDto> {
        return restWithUserRole().getForEntity<Array<QuizQuestionUserAnswerDto>>("/quiz/$quizId/question/user-answer").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }
}
