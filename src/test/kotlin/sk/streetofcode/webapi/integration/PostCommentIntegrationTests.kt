package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.webapi.api.dto.PostCommentDto
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.PostCommentAddRequest
import sk.streetofcode.webapi.api.request.PostCommentEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class PostCommentIntegrationTests : IntegrationTests() {

    companion object {
        const val dummyCommentText = "commentText"
        const val dummyPostTitle = "postTitle"
    }

    init {
        "get post comments" {
            val commentsResponse = getPostComments("1")
            commentsResponse.size shouldBe 2
        }

        "add post comment" {
            val postId = "2"
            val comment = addPostComment(postId, PostCommentAddRequest(dummyPostTitle, dummyCommentText))

            val fetchedComments = getPostComments(postId)
            fetchedComments.size shouldBe 1

            val fetchedComment = fetchedComments[0]
            fetchedComment.commentText shouldBe dummyCommentText
            fetchedComment.postTitle shouldBe dummyPostTitle

            fetchedComment.id shouldBe comment.id
            fetchedComment.updatedAt.toEpochSecond() shouldBe comment.updatedAt.toEpochSecond()
            fetchedComment.userId shouldBe comment.userId
        }

        "add anonymous post comment" {
            val postId = "3"
            val comment = addPostCommentAnonymous(postId, PostCommentAddRequest(dummyPostTitle, dummyCommentText))

            val fetchedComments = getPostComments(postId)
            fetchedComments.size shouldBe 1

            val fetchedComment = fetchedComments[0]
            fetchedComment.commentText shouldBe dummyCommentText
            fetchedComment.postTitle shouldBe dummyPostTitle

            fetchedComment.id shouldBe comment.id
            fetchedComment.updatedAt.toEpochSecond() shouldBe comment.updatedAt.toEpochSecond()
            fetchedComment.userId shouldBe null
        }

        "edit post comment" {
            val postId = "2"
            val addedComment = addPostComment(postId, PostCommentAddRequest(dummyPostTitle, dummyCommentText))

            val editedCommentText = "editedCommentText"
            val editedComment = editPostComment(postId, addedComment.id, PostCommentEditRequest(editedCommentText))

            editedComment.commentText shouldBe editedCommentText
            editedComment.postTitle shouldBe dummyPostTitle
            editedComment.id shouldBe addedComment.id
            editedComment.updatedAt.toEpochSecond() shouldBe addedComment.updatedAt.toEpochSecond()
            editedComment.userId shouldBe addedComment.userId
        }

        "fail, edit post comment, post not found" {
            editPostCommentNotFound("1", 99, PostCommentEditRequest(dummyCommentText))
        }

        "fail, edit post comment, postId not correct" {
            editPostCommentBadRequest("99", 1, PostCommentEditRequest(dummyCommentText))
        }

        "delete post comment" {
            val postId = "2"

            getPostComments(postId).size shouldBe 2
            val addedComment = addPostComment(postId, PostCommentAddRequest(dummyPostTitle, dummyCommentText))
            getPostComments(postId).size shouldBe 3
            deletePostComment(postId, addedComment.id)
            getPostComments(postId).size shouldBe 2

            deletePostCommentNotFound(postId, addedComment.id)
        }

        "fail, delete post comment, comment not found" {
            deletePostCommentNotFound("1", 99)
        }

        "fail, delete post comment, postId not correct" {
            deletePostCommentBadRequest("99", 1)
        }
    }

    private fun getPostComments(postId: String): List<PostCommentDto> {
        return restWithUserRole().getForEntity<Array<PostCommentDto>>("/post/$postId/comment").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    private fun addPostComment(postId: String, body: PostCommentAddRequest): PostCommentDto {
        return restWithUserRole().postForEntity<PostCommentDto>("/post/$postId/comment", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun addPostCommentAnonymous(postId: String, body: PostCommentAddRequest): PostCommentDto {
        return restTemplate.postForEntity<PostCommentDto>("/post/$postId/comment", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editPostComment(postId: String, commentId: Long, body: PostCommentEditRequest): PostCommentDto {
        return restWithUserRole().putForEntity<PostCommentDto>("/post/$postId/comment/$commentId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun editPostCommentNotFound(postId: String, commentId: Long, body: PostCommentEditRequest) {
        return restWithUserRole().putForEntity<ResourceNotFoundException>("/post/$postId/comment/$commentId", body)
            .let {
                it.statusCode shouldBe HttpStatus.NOT_FOUND
            }
    }

    private fun editPostCommentBadRequest(postId: String, commentId: Long, body: PostCommentEditRequest) {
        return restWithUserRole().putForEntity<ResourceNotFoundException>("/post/$postId/comment/$commentId", body)
            .let {
                it.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
    }

    private fun deletePostComment(postId: String, commentId: Long) {
        return restWithUserRole().delete("/post/$postId/comment/$commentId")
    }

    private fun deletePostCommentNotFound(postId: String, commentId: Long) {
        return restWithUserRole().deleteForEntity<ResourceNotFoundException>("/post/$postId/comment/$commentId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun deletePostCommentBadRequest(postId: String, commentId: Long) {
        return restWithUserRole().deleteForEntity<ResourceNotFoundException>("/lecture/$postId/comment/$commentId")
            .let {
                it.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
    }
}
