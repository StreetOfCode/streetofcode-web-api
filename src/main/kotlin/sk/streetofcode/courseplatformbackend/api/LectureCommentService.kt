package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.LectureCommentDto
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentEditRequest
import java.util.UUID

interface LectureCommentService {
    fun getAll(lectureId: Long): List<LectureCommentDto>
    fun add(userId: UUID, lectureId: Long, addRequest: LectureCommentAddRequest): LectureCommentDto
    fun edit(userId: UUID, lectureId: Long, commentId: Long, editRequest: LectureCommentEditRequest): LectureCommentDto
    fun delete(userId: UUID, lectureId: Long, commentId: Long)
}
