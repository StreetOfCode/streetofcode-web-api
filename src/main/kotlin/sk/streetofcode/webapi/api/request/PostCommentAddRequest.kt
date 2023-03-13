package sk.streetofcode.webapi.api.request

data class PostCommentAddRequest(
    val postSlug: String,
    val commentText: String,
)
