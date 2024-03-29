package sk.streetofcode.webapi.api.request

data class SocUserAddRequest(
    val id: String,
    val name: String,
    val email: String,
    val imageUrl: String?,
    val receiveNewsletter: Boolean,
    val sendDiscordInvitation: Boolean,
    val subscribedFrom: String
)
