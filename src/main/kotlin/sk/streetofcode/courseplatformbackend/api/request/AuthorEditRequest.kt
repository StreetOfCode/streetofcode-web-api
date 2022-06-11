package sk.streetofcode.courseplatformbackend.api.request

data class AuthorEditRequest(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val coursesTitle: String,
    val email: String,
    val description: String
)
