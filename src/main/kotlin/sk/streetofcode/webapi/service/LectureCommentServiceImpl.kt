package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.LectureCommentService
import sk.streetofcode.webapi.api.SocUserService
import sk.streetofcode.webapi.api.dto.LectureCommentDto
import sk.streetofcode.webapi.api.exception.AuthorizationException
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.LectureCommentAddRequest
import sk.streetofcode.webapi.api.request.LectureCommentEditRequest
import sk.streetofcode.webapi.db.repository.LectureCommentRepository
import sk.streetofcode.webapi.db.repository.LectureRepository
import sk.streetofcode.webapi.model.LectureComment
import sk.streetofcode.webapi.model.toLectureCommentDto
import java.time.OffsetDateTime

@Service
class LectureCommentServiceImpl(
    private val lectureRepository: LectureRepository,
    private val lectureCommentRepository: LectureCommentRepository,
    private val authenticationService: AuthenticationService,
    private val socUserService: SocUserService
) : LectureCommentService {

    companion object {
        private val log = LoggerFactory.getLogger(LectureCommentServiceImpl::class.java)
    }

    override fun getAll(lectureId: Long): List<LectureCommentDto> {
        return lectureCommentRepository.findAllByLectureId(lectureId).map { it.toLectureCommentDto() }
    }

    override fun add(userId: String, lectureId: Long, addRequest: LectureCommentAddRequest): LectureCommentDto {
        val lecture = lectureRepository.findById(lectureId).orElseThrow { ResourceNotFoundException("Lecture with id $lectureId was not found") }

        try {
            return lectureCommentRepository
                .save(LectureComment(socUserService.get(authenticationService.getUserId()), lecture, addRequest.commentText))
                .toLectureCommentDto()
        } catch (e: Exception) {
            log.error("Problem with saving lectureComment to db", e)
            throw InternalErrorException("Could not save lecture comment")
        }
    }

    override fun edit(
        userId: String,
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
            comment.commentText = editRequest.commentText
            comment.updatedAt = OffsetDateTime.now()

            return lectureCommentRepository.save(comment).toLectureCommentDto()
        } catch (e: Exception) {
            log.error("Problem with editing lectureComment", e)
            throw InternalErrorException("Could not edit lecture comment")
        }
    }

    override fun delete(userId: String, lectureId: Long, commentId: Long) {
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
        if (comment.socUser.firebaseId != userId && !authenticationService.isAdmin()) {
            throw AuthorizationException()
        }
    }
}
