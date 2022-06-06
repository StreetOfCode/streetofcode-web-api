package sk.streetofcode.courseplatformbackend.service.quiz

import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.jboss.logging.Logger
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.QuizQuestionUserAnswerService
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionAnswerCorrectnessDto
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionUserAnswerDto
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionUserAnswerRequest
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizQuestionAnswerRepository
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizQuestionRepository
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizQuestionUserAnswerRepository
import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestionUserAnswer
import sk.streetofcode.courseplatformbackend.model.quiz.toQuizQuestionUserAnswerDto
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@Service
class QuizQuestionUserAnswerServiceImpl(
    val quizQuestionRepository: QuizQuestionRepository,
    val quizQuestionUserAnswerRepository: QuizQuestionUserAnswerRepository,
    val quizQuestionAnswerRepository: QuizQuestionAnswerRepository,
    val authenticationService: AuthenticationService
) : QuizQuestionUserAnswerService {
    companion object {
        val logger: Logger? = LoggerFactory.logger(QuizQuestionUserAnswerServiceImpl::class.java)
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

            mostRecentAnswers.map { it.toQuizQuestionUserAnswerDto() }
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
            logger?.warn("There are multiple answer entries for one question and one user!!! (Something is wrong)")
        }

        for (answerId in answerRequest.answerIds) {
            val answer =
                quizQuestionAnswerRepository.findById(answerId)
                    .orElseThrow { ResourceNotFoundException("QuizQuestionAnswer with id $answerId was not found") }

            quizQuestionUserAnswerRepository.deleteByQuestionIdAndUserId(answerRequest.questionId, userId)

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
