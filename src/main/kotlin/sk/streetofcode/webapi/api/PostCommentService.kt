package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.PostCommentDto
import sk.streetofcode.webapi.api.request.PostCommentAddRequest
import sk.streetofcode.webapi.api.request.PostCommentEditRequest

interface PostCommentService {
    fun getAll(postId: String): List<PostCommentDto>
    fun add(userId: String? = null, postId: String, addRequest: PostCommentAddRequest): PostCommentDto
    fun edit(userId: String? = null, postId: String, commentId: Long, editRequest: PostCommentEditRequest): PostCommentDto
    fun delete(userId: String? = null, postId: String, commentId: Long)
}
