package sk.streetofcode.courseplatformbackend.api.request

data class AuthorEditRequest(
    val id: Long,
    val name: String,
    val url: String,
    val description: String
)
