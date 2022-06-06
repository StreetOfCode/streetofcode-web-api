package sk.streetofcode.courseplatformbackend.service.quiz

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.QuizQuestionAnswerService
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionAnswerDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.ConflictException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionAnswerAddRequest
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionAnswerEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizQuestionAnswerRepository
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizQuestionRepository
import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestionAnswer
import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestionType
import sk.streetofcode.courseplatformbackend.model.quiz.toQuizQuestionAnswerDto

@Service
class QuizQuestionAnswerServiceImpl(
    val quizQuestionAnswerRepository: QuizQuestionAnswerRepository,
    val quizQuestionRepository: QuizQuestionRepository
) : QuizQuestionAnswerService {
    override fun get(id: Long): QuizQuestionAnswerDto {
        return quizQuestionAnswerRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("QuizQuestionAnswer with id $id was not found") }
            .toQuizQuestionAnswerDto()
    }

    override fun getAll(): List<QuizQuestionAnswerDto> {
        return quizQuestionAnswerRepository
            .findAll()
            .map { it.toQuizQuestionAnswerDto() }
    }

    override fun getAllForQuestion(questionId: Long): List<QuizQuestionAnswerDto> {
        return quizQuestionAnswerRepository
            .findByQuestionId(questionId)
            .map { it.toQuizQuestionAnswerDto() }
    }

    override fun add(addRequest: QuizQuestionAnswerAddRequest): QuizQuestionAnswerDto {
        val question = quizQuestionRepository
            .findById(addRequest.questionId)
            .orElseThrow { ResourceNotFoundException("QuizQuestion with id ${addRequest.questionId} was not found") }

        if (question.type == QuizQuestionType.SINGLE_CHOICE &&
            question.correctAnswers.size != 0 &&
            addRequest.isCorrect
        ) {
            throw ConflictException("Question with id ${addRequest.questionId} is single choice and already has a correct answer.")
        }

        return quizQuestionAnswerRepository.save(
            QuizQuestionAnswer(
                question = question,
                text = addRequest.text,
                isCorrect = addRequest.isCorrect
            )
        ).toQuizQuestionAnswerDto()
    }

    override fun edit(id: Long, editRequest: QuizQuestionAnswerEditRequest): QuizQuestionAnswerDto {
        if (id != editRequest.id) {
            throw BadRequestException("Path variable id is not equal to request id field")
        }

        val question = quizQuestionRepository
            .findById(editRequest.questionId)
            .orElseThrow { ResourceNotFoundException("QuizQuestion with id ${editRequest.id} was not found") }

        val questionAnswer = quizQuestionAnswerRepository
            .findById(editRequest.id)
            .orElseThrow { ResourceNotFoundException("QuizQuestionAnswer with id ${editRequest.id} was not found") }

        if (question.type == QuizQuestionType.SINGLE_CHOICE &&
            question.correctAnswers.size != 0 &&
            editRequest.isCorrect
        ) {
            throw ConflictException("Question with id ${editRequest.questionId} is single choice and already has a correct answer.")
        }

        questionAnswer.question = question
        questionAnswer.text = editRequest.text
        questionAnswer.isCorrect = editRequest.isCorrect

        return quizQuestionAnswerRepository.save(questionAnswer).toQuizQuestionAnswerDto()
    }

    override fun delete(id: Long): QuizQuestionAnswerDto {
        val answer = quizQuestionAnswerRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("QuizQuestionAnswer with id $id was not found") }

        quizQuestionAnswerRepository.deleteById(id)

        return answer.toQuizQuestionAnswerDto()
    }
}
