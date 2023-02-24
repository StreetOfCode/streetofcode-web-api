package sk.streetofcode.webapi.api.request

data class PostCommentAddRequest(
    val postTitle: String,
    val commentText: String,
)
