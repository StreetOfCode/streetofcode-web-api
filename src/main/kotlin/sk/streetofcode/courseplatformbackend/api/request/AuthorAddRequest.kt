package sk.streetofcode.courseplatformbackend.api.request

data class AuthorAddRequest(
    val name: String,
    val slug: String,
    val imageUrl: String,
    val coursesTitle: String,
    val email: String,
    val description: String
)
