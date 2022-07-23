package sk.streetofcode.courseplatformbackend.api.request

data class UserEditRequest(
    val name: String,
    val imageUrl: String?,
    val receiveNewsletter: Boolean
)
