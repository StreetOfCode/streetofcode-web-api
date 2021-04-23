package sk.streetofcode.courseplatformbackend.api.request

data class AuthorAddRequest(
    val name: String,
    val url: String,
    val description: String
)
