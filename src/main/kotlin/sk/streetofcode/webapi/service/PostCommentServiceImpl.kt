package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.EmailService
import sk.streetofcode.webapi.api.PostCommentService
import sk.streetofcode.webapi.api.SocUserService
import sk.streetofcode.webapi.api.dto.PostCommentDto
import sk.streetofcode.webapi.api.exception.AuthorizationException
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.PostCommentAddRequest
import sk.streetofcode.webapi.api.request.PostCommentEditRequest
import sk.streetofcode.webapi.db.repository.PostCommentRepository
import sk.streetofcode.webapi.model.PostComment
import sk.streetofcode.webapi.model.toPostCommentDto
import java.time.OffsetDateTime

@Service
class PostCommentServiceImpl(
    private val postCommentRepository: PostCommentRepository,
    private val authenticationService: AuthenticationService,
    private val socUserService: SocUserService,
    private val emailService: EmailService
) : PostCommentService {

    companion object {
        private val log = LoggerFactory.getLogger(PostCommentServiceImpl::class.java)
    }

    override fun getAll(postId: String): List<PostCommentDto> {
        return postCommentRepository.findAllByPostId(postId).map { it.toPostCommentDto() }.sortedBy { it.createdAt }
    }

    override fun add(userId: String?, postId: String, addRequest: PostCommentAddRequest): PostCommentDto {
        try {
            val postComment = postCommentRepository
                .save(
                    PostComment(
                        if (userId != null) socUserService.get(authenticationService.getUserId()) else null,
                        postId,
                        addRequest.postSlug,
                        addRequest.commentText
                    )
                )

            emailService.sendNewPostCommentNotification(postComment)

            return postComment.toPostCommentDto()
        } catch (e: Exception) {
            log.error("Problem with saving postComment to db", e)
            throw InternalErrorException("Could not save post comment")
        }
    }

    override fun edit(
        userId: String?,
        postId: String,
        commentId: Long,
        editRequest: PostCommentEditRequest
    ): PostCommentDto {
        val comment = postCommentRepository.findById(commentId)
            .orElseThrow { ResourceNotFoundException("Post comment with id $commentId was not found") }
        if (postId != comment.postId) {
            throw BadRequestException("Comment has different postId")
        }

        validateUserAuthorization(comment)

        try {
            comment.commentText = editRequest.commentText
            comment.updatedAt = OffsetDateTime.now()

            return postCommentRepository.save(comment).toPostCommentDto()
        } catch (e: Exception) {
            log.error("Problem with editing postComment", e)
            throw InternalErrorException("Could not edit post comment")
        }
    }

    override fun delete(userId: String?, postId: String, commentId: Long) {
        val comment = postCommentRepository.findById(commentId)
            .orElseThrow { ResourceNotFoundException("Post comment with id $commentId was not found") }
        if (postId != comment.postId) {
            throw BadRequestException("Comment has different postId")
        }

        validateUserAuthorization(comment)

        postCommentRepository.deleteById(commentId)
    }

    // Only user who written the comment and admin can manipulate with it
    private fun validateUserAuthorization(comment: PostComment) {
        if (comment.socUser != null) {
            val userId = authenticationService.getUserId()
            if (comment.socUser.firebaseId != userId && !authenticationService.isAdmin()) {
                throw AuthorizationException()
            }
        }
    }
}
