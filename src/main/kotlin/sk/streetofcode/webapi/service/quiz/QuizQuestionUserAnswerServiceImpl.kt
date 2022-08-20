package sk.streetofcode.webapi.service.quiz

import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.QuizQuestionUserAnswerService
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionAnswerCorrectnessDto
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionUserAnswerDto
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.QuizQuestionUserAnswerRequest
import sk.streetofcode.webapi.db.repository.quiz.QuizQuestionAnswerRepository
import sk.streetofcode.webapi.db.repository.quiz.QuizQuestionRepository
import sk.streetofcode.webapi.db.repository.quiz.QuizQuestionUserAnswerRepository
import sk.streetofcode.webapi.model.quiz.QuizQuestionUserAnswer
import sk.streetofcode.webapi.model.quiz.toQuizQuestionUserAnswerDto
import sk.streetofcode.webapi.service.AuthenticationService

@Service
class QuizQuestionUserAnswerServiceImpl(
    val quizQuestionRepository: QuizQuestionRepository,
    val quizQuestionUserAnswerRepository: QuizQuestionUserAnswerRepository,
    val quizQuestionAnswerRepository: QuizQuestionAnswerRepository,
    val authenticationService: AuthenticationService
) : QuizQuestionUserAnswerService {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(QuizQuestionUserAnswerServiceImpl::class.java)
    }
    override fun get(id: Long): QuizQuestionUserAnswerDto {
        return quizQuestionUserAnswerRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("QuizQuestionUserAnswer with id $id was not found") }
            .toQuizQuestionUserAnswerDto()
    }

    override fun getAll(): List<QuizQuestionUserAnswerDto> {
        return quizQuestionUserAnswerRepository
            .findAll()
            .map { it.toQuizQuestionUserAnswerDto() }
    }

    override fun getAllAnswersForQuestion(questionId: Long): List<QuizQuestionUserAnswerDto> {
        return quizQuestionUserAnswerRepository
            .findByQuestionId(questionId)
            .map { it.toQuizQuestionUserAnswerDto() }
    }

    override fun getAllUserAnswersForQuiz(quizId: Long): List<QuizQuestionUserAnswerDto> {
        val userId = authenticationService.getUserId()

        val answers = quizQuestionUserAnswerRepository
            .findByQuestionQuizIdAndUserId(quizId, userId)
            .sortedBy { -it.tryCount }

        return if (answers.isEmpty()) {
            listOf()
        } else {
            val mostRecentAnswers = answers
                .filter {
                    it.tryCount == answers.filter { x -> x.question.id == it.question.id }.maxOf { x -> x.tryCount }
                }

            mostRecentAnswers.map { answer ->
                val answer = answer.toQuizQuestionUserAnswerDto()
                val question = mostRecentAnswers.first { it.question.id == answer.question.id }.question

                answer.isCorrect =
                    question.correctAnswers
                    .map { it.id }.sortedBy { it } == mostRecentAnswers.filter { it.question.id == question.id }.map { it.answer.id }.sortedBy { it }

                answer
            }
        }
    }

    override fun answer(answerRequest: QuizQuestionUserAnswerRequest): QuizQuestionAnswerCorrectnessDto {
        val question = quizQuestionRepository
            .findById(answerRequest.questionId)
            .orElseThrow { ResourceNotFoundException("QuizQuestion with id ${answerRequest.questionId} was not found") }

        val userId = authenticationService.getUserId()

        val previousUserAnswers = quizQuestionUserAnswerRepository
            .findByQuestionIdAndUserId(answerRequest.questionId, userId)

        if (previousUserAnswers.size > 1) {
            log.warn("There are multiple answer entries for one question and one user!!! (Something is wrong)")
        }

        quizQuestionUserAnswerRepository.deleteByQuestionIdAndUserId(answerRequest.questionId, userId)

        for (answerId in answerRequest.answerIds) {
            val answer =
                quizQuestionAnswerRepository.findById(answerId)
                    .orElseThrow { ResourceNotFoundException("QuizQuestionAnswer with id $answerId was not found") }

            val tryCount = if (previousUserAnswers.isEmpty()) {
                0
            } else {
                previousUserAnswers.first().tryCount
            }
            quizQuestionUserAnswerRepository.save(
                QuizQuestionUserAnswer(
                    question = question,
                    answer = answer,
                    userId = userId,
                    tryCount = tryCount + 1
                )
            )
        }

        return QuizQuestionAnswerCorrectnessDto(
            question.correctAnswers.map { it.id }.sortedBy { it } == answerRequest.answerIds.sorted()
        )
    }
}
