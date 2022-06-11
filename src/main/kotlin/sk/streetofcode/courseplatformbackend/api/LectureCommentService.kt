package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.LectureCommentDto
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureCommentEditRequest

interface LectureCommentService {
    fun getAll(lectureId: Long): List<LectureCommentDto>
    fun add(userId: String, lectureId: Long, addRequest: LectureCommentAddRequest): LectureCommentDto
    fun edit(userId: String, lectureId: Long, commentId: Long, editRequest: LectureCommentEditRequest): LectureCommentDto
    fun delete(userId: String, lectureId: Long, commentId: Long)
}
