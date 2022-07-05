package sk.streetofcode.courseplatformbackend.api.request

data class UserEditRequest(
    val name: String,
    val email: String,
    val imageUrl: String?,
    val receiveNewsletter: Boolean
)
