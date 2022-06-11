package sk.streetofcode.courseplatformbackend.api.dto

import java.time.OffsetDateTime

data class LectureCommentDto(
    val id: Long,
    val userId: String,
    val userName: String,
    val commentText: String,
    val updatedAt: OffsetDateTime
)
