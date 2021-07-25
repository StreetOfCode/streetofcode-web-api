package sk.streetofcode.courseplatformbackend.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.LectureCommentService
import sk.streetofcode.courseplatformbackend.api.dto.LectureCommentDto
import sk.streetofcode.courseplatformbackend.api.exception.AuthorizationException
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.LectureCommentRepository
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.model.LectureComment
import sk.streetofcode.courseplatformbackend.model.toLectureCommentDto
import java.time.OffsetDateTime
import java.util.UUID

@Service
class LectureCommentServiceImpl(
    private val lectureRepository: LectureRepository,
    private val lectureCommentRepository: LectureCommentRepository,
    private val authenticationService: AuthenticationService
) : LectureCommentService {

    companion object {
        private val log = LoggerFactory.getLogger(LectureCommentServiceImpl::class.java)
    }

    override fun getAll(lectureId: Long): List<LectureCommentDto> {
        return lectureCommentRepository.findAllByLectureId(lectureId).map { it.toLectureCommentDto() }
    }

    override fun add(userId: UUID, lectureId: Long, addRequest: LectureCommentAddRequest): LectureCommentDto {
        val lecture = lectureRepository.findById(lectureId).orElseThrow { ResourceNotFoundException("Lecture with id $lectureId was not found") }

        try {
            return lectureCommentRepository
                .save(LectureComment(userId, lecture, addRequest.userName, addRequest.commentText))
                .toLectureCommentDto()
        } catch (e: Exception) {
            log.error("Problem with saving lectureComment to db", e)
            throw InternalErrorException("Could not save lecture comment")
        }
    }

    override fun edit(
        userId: UUID,
        lectureId: Long,
        commentId: Long,
        editRequest: LectureCommentEditRequest
    ): LectureCommentDto {
        val comment = lectureCommentRepository.findById(commentId).orElseThrow { ResourceNotFoundException("Lecture comment with id $commentId was not found") }
        if (lectureId != comment.lecture.id) {
            throw BadRequestException("Comment has different lectureId")
        }

        validateUserAuthorization(comment)

        try {
            comment.userName = editRequest.userName
            comment.commentText = editRequest.commentText
            comment.updatedAt = OffsetDateTime.now()

            return lectureCommentRepository.save(comment).toLectureCommentDto()
        } catch (e: Exception) {
            log.error("Problem with editing lectureComment", e)
            throw InternalErrorException("Could not edit lecture comment")
        }
    }

    override fun delete(userId: UUID, lectureId: Long, commentId: Long) {
        val comment = lectureCommentRepository.findById(commentId).orElseThrow { ResourceNotFoundException("Lecture comment with id $commentId was not found") }
        if (lectureId != comment.lecture.id) {
            throw BadRequestException("Comment has different lectureId")
        }

        validateUserAuthorization(comment)

        lectureCommentRepository.deleteById(commentId)
    }

    // Only user who written the comment and admin can manipulate with it
    private fun validateUserAuthorization(comment: LectureComment) {
        val userId = authenticationService.getUserId()
        if (comment.userId != userId && !authenticationService.isAdmin()) {
            throw AuthorizationException()
        }
    }
}
