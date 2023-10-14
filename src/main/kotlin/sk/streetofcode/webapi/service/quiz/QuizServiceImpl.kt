package sk.streetofcode.webapi.service.quiz

import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.QuizService
import sk.streetofcode.webapi.api.dto.quiz.QuizDto
import sk.streetofcode.webapi.api.exception.AuthorizationException
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.QuizAddRequest
import sk.streetofcode.webapi.api.request.QuizEditRequest
import sk.streetofcode.webapi.db.repository.LectureRepository
import sk.streetofcode.webapi.db.repository.quiz.QuizRepository
import sk.streetofcode.webapi.model.quiz.Quiz
import sk.streetofcode.webapi.model.quiz.toQuizDto

@Service
class QuizServiceImpl(
    val lectureRepository: LectureRepository,
    val quizRepository: QuizRepository,
    val courseProductService: CourseProductService
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
        val quizzes = quizRepository.findByLectureId(lectureId)
        if (quizzes.isEmpty()) return listOf()

        val courseId = quizzes.first().lecture.chapter.course.id!!

        // TODO paid-courses: allow if lecture preview is allowed
        if (!courseProductService.isOwnedByUser(courseId).isOwnedByUser
            // && !quizzes.first().lecture.isPreviewAllowed
        ) {
            throw AuthorizationException("User does not own this course")
        }

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
