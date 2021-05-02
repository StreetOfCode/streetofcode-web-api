package sk.streetofcode.courseplatformbackend.api.dto

import java.time.OffsetDateTime
import java.util.UUID

data class LectureCommentDto(
    val id: Long,
    val userId: UUID,
    val userName: String,
    val commentText: String,
    val updatedAt: OffsetDateTime
)
