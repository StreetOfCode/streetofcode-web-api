package sk.streetofcode.courseplatformbackend.api.request

data class UserAddRequest(
    val id: String,
    val name: String,
    val email: String,
    val imageUrl: String?,
    val receiveNewsletter: Boolean,
    val sendDiscordInvitation: Boolean
)
