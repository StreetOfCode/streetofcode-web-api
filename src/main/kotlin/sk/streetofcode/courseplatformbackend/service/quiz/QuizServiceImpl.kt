package sk.streetofcode.courseplatformbackend.service.quiz

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.QuizService
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.QuizAddRequest
import sk.streetofcode.courseplatformbackend.api.request.QuizEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.db.repository.quiz.QuizRepository
import sk.streetofcode.courseplatformbackend.model.quiz.Quiz
import sk.streetofcode.courseplatformbackend.model.quiz.toQuizDto

@Service
class QuizServiceImpl(
    val lectureRepository: LectureRepository,
    val quizRepository: QuizRepository
) : QuizService {
    override fun get(id: Long): QuizDto {
        return quizRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Quiz with id $id was not found") }
            .toQuizDto()
    }

    override fun getAll(): List<QuizDto> {
        return quizRepository
            .findAll()
            .map { it.toQuizDto() }
    }

    override fun getAllForLecture(lectureId: Long): List<QuizDto> {
        return quizRepository.findByLectureId(lectureId)
            .map { it.toQuizDto() }
    }

    override fun add(addRequest: QuizAddRequest): QuizDto {
        val lecture = lectureRepository
            .findById(addRequest.lectureId)
            .orElseThrow { ResourceNotFoundException("Lecture with id ${addRequest.lectureId} was not found") }

        return quizRepository.save(
            Quiz(
                lecture = lecture,
                title = addRequest.title,
                subtitle = addRequest.subtitle,
                finishedMessage = addRequest.finishedMessage
            )
        ).toQuizDto()
    }

    override fun edit(id: Long, editRequest: QuizEditRequest): QuizDto {
        if (id != editRequest.id) {
            throw BadRequestException("Path variable id is not equal to request id field")
        }

        val lecture = lectureRepository
            .findById(editRequest.lectureId)
            .orElseThrow { ResourceNotFoundException("Lecture with id ${editRequest.lectureId} was not found") }

        val quiz = quizRepository
            .findById(editRequest.id)
            .orElseThrow { ResourceNotFoundException("Quiz with id ${editRequest.id} was not found") }

        quiz.lecture = lecture
        quiz.finishedMessage = editRequest.finishedMessage
        quiz.subtitle = editRequest.subtitle
        quiz.title = editRequest.title

        return quizRepository.save(quiz).toQuizDto()
    }

    override fun delete(id: Long): QuizDto {
        val quiz = quizRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Quiz with id $id was not found") }

        quizRepository.deleteById(id)

        return quiz.toQuizDto()
    }
}
