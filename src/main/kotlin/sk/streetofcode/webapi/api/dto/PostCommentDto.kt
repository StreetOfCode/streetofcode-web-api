package sk.streetofcode.webapi.api.dto

import java.time.OffsetDateTime

data class PostCommentDto(
    val id: Long,
    val postId: String,
    val postTitle: String,
    val userId: String?,
    val userName: String?,
    val imageUrl: String?,
    val commentText: String,
    val updatedAt: OffsetDateTime
)
