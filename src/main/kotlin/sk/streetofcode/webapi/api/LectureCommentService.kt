package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.LectureCommentDto
import sk.streetofcode.webapi.api.request.LectureCommentAddRequest
import sk.streetofcode.webapi.api.request.LectureCommentEditRequest

interface LectureCommentService {
    fun getAll(lectureId: Long): List<LectureCommentDto>
    fun add(userId: String, lectureId: Long, addRequest: LectureCommentAddRequest): LectureCommentDto
    fun edit(
        userId: String,
        lectureId: Long,
        commentId: Long,
        editRequest: LectureCommentEditRequest
    ): LectureCommentDto

    fun delete(userId: String, lectureId: Long, commentId: Long)
}
