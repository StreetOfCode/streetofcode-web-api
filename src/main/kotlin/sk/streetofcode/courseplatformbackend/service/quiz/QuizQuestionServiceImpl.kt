package sk.streetofcode.courseplatformbackend.service.quiz

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.QuizQuestionService
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionAddRequest
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizQuestionRepository
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizRepository
import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestion
import sk.streetofcode.courseplatformbackend.model.quiz.toQuizQuestionDto

@Service
class QuizQuestionServiceImpl(
    val quizQuestionRepository: QuizQuestionRepository,
    val quizRepository: QuizRepository
) : QuizQuestionService {
    override fun get(id: Long): QuizQuestionDto {
        return quizQuestionRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("QuizQuestion with id $id was not found") }
            .toQuizQuestionDto()
    }

    override fun getAll(): List<QuizQuestionDto> {
        return quizQuestionRepository
            .findAll()
            .map { it.toQuizQuestionDto() }
    }

    override fun getAllForQuiz(quizId: Long): List<QuizQuestionDto> {
        return quizQuestionRepository.findByQuizId(quizId)
            .map { it.toQuizQuestionDto() }
    }

    override fun add(addRequest: QuizQuestionAddRequest): QuizQuestionDto {
        val quiz = quizRepository
            .findById(addRequest.quizId)
            .orElseThrow { ResourceNotFoundException("Quiz with id ${addRequest.quizId} was not found") }

        return quizQuestionRepository.save(
            QuizQuestion(
                quiz = quiz,
                questionOrder = addRequest.questionOrder,
                text = addRequest.text,
                type = addRequest.type
            )
        ).toQuizQuestionDto()
    }

    override fun edit(id: Long, editRequest: QuizQuestionEditRequest): QuizQuestionDto {
        if (id != editRequest.id) {
            throw BadRequestException("Path variable id is not equal to request id field")
        }

        val quiz = quizRepository
            .findById(editRequest.quizId)
            .orElseThrow { ResourceNotFoundException("Quiz with id ${editRequest.quizId} was not found") }

        val question = quizQuestionRepository
            .findById(editRequest.id)
            .orElseThrow { ResourceNotFoundException("QuizQuestion with id ${editRequest.id} was not found") }

        question.questionOrder = editRequest.questionOrder
        question.quiz = quiz
        question.text = editRequest.text
        question.type = editRequest.type

        return quizQuestionRepository.save(question).toQuizQuestionDto()
    }

    override fun delete(id: Long): QuizQuestionDto {
        val question = quizQuestionRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("QuizQuestion with id $id was not found") }

        quizQuestionRepository.deleteById(id)

        return question.toQuizQuestionDto()
    }
}
