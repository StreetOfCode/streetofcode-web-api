package sk.streetofcode.webapi.api.dto

import java.time.OffsetDateTime

data class PostCommentDto(
    val id: Long,
    val postId: String,
    val postSlug: String,
    val userId: String?,
    val userName: String?,
    val imageUrl: String?,
    val commentText: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
