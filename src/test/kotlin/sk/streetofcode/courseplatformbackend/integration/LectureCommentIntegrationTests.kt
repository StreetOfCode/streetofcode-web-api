package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.courseplatformbackend.api.dto.LectureCommentDto
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentEditRequest
import sk.streetofcode.courseplatformbackend.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class LectureCommentIntegrationTests : IntegrationTests() {

    companion object {
        const val dummyCommentText = "commentText"
    }

    init {
        "get lecture comments" {
            val commentsResponse = getLectureComments(1)
            commentsResponse.size shouldBe 2
        }

        "add lecture comment" {
            val lectureId = 2L
            val comment = addLectureComment(lectureId, LectureCommentAddRequest(dummyCommentText))

            val fetchedComments = getLectureComments(lectureId)
            fetchedComments.size shouldBe 1

            val fetchedComment = fetchedComments[0]
            fetchedComment.commentText shouldBe dummyCommentText

            fetchedComment.id shouldBe comment.id
            fetchedComment.updatedAt.toEpochSecond() shouldBe comment.updatedAt.toEpochSecond()
            fetchedComment.userId shouldBe comment.userId
        }

        "fail add lecture comment, lecture not found" {
            addLectureCommentNotFoundLecture(
                99L,
                LectureCommentAddRequest(dummyCommentText)
            )
        }

        "edit lecture comment" {
            val lectureId = 2L
            val addedComment = addLectureComment(lectureId, LectureCommentAddRequest(dummyCommentText))

            val editedCommentText = "editedCommentText"
            val editedComment = editLectureComment(lectureId, addedComment.id, LectureCommentEditRequest(editedCommentText))

            editedComment.commentText shouldBe editedCommentText
            editedComment.id shouldBe addedComment.id
            editedComment.updatedAt.toEpochSecond() shouldBe addedComment.updatedAt.toEpochSecond()
            editedComment.userId shouldBe addedComment.userId
        }

        "fail, edit lecture comment, comment not found" {
            editLectureCommentNotFound(1, 99, LectureCommentEditRequest(dummyCommentText))
        }

        "fail, edit lecture comment, lectureId not correct" {
            editLectureCommentBadRequest(99, 1, LectureCommentEditRequest(dummyCommentText))
        }

        "delete lecture comment" {
            val lectureId = 2L

            getLectureComments(lectureId).size shouldBe 2
            val addedComment = addLectureComment(lectureId, LectureCommentAddRequest(dummyCommentText))
            getLectureComments(lectureId).size shouldBe 3
            deleteLectureComment(lectureId, addedComment.id)
            getLectureComments(lectureId).size shouldBe 2

            deleteLectureCommentNotFound(lectureId, addedComment.id)
        }

        "fail, delete lecture comment, comment not found" {
            deleteLectureCommentNotFound(1, 99)
        }

        "fail, delete lecture comment, lectureId not correct" {
            deleteLectureCommentBadRequest(99, 1)
        }
    }

    private fun getLectureComments(lectureId: Long): List<LectureCommentDto> {
        return restWithAdminRole().getForEntity<Array<LectureCommentDto>>("/lecture/$lectureId/comment").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    private fun addLectureComment(lectureId: Long, body: LectureCommentAddRequest): LectureCommentDto {
        return restWithAdminRole().postForEntity<LectureCommentDto>("/lecture/$lectureId/comment", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editLectureComment(lectureId: Long, commentId: Long, body: LectureCommentEditRequest): LectureCommentDto {
        return restWithAdminRole().putForEntity<LectureCommentDto>("/lecture/$lectureId/comment/$commentId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun addLectureCommentNotFoundLecture(lectureId: Long, body: LectureCommentAddRequest) {
        return restWithAdminRole().postForEntity<ResourceNotFoundException>("/lecture/$lectureId/comment", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editLectureCommentNotFound(lectureId: Long, commentId: Long, body: LectureCommentEditRequest) {
        return restWithAdminRole().putForEntity<ResourceNotFoundException>("/lecture/$lectureId/comment/$commentId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editLectureCommentBadRequest(lectureId: Long, commentId: Long, body: LectureCommentEditRequest) {
        return restWithAdminRole().putForEntity<ResourceNotFoundException>("/lecture/$lectureId/comment/$commentId", body).let {
            it.statusCode shouldBe HttpStatus.BAD_REQUEST
        }
    }

    private fun deleteLectureComment(lectureId: Long, commentId: Long) {
        return restWithAdminRole().delete("/lecture/$lectureId/comment/$commentId")
    }

    private fun deleteLectureCommentNotFound(lectureId: Long, commentId: Long) {
        return restWithAdminRole().deleteForEntity<ResourceNotFoundException>("/lecture/$lectureId/comment/$commentId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun deleteLectureCommentBadRequest(lectureId: Long, commentId: Long) {
        return restWithAdminRole().deleteForEntity<ResourceNotFoundException>("/lecture/$lectureId/comment/$commentId").let {
            it.statusCode shouldBe HttpStatus.BAD_REQUEST
        }
    }
}
