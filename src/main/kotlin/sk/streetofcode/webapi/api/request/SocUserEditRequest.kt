package sk.streetofcode.webapi.api.request

data class SocUserEditRequest(
    val name: String,
    val imageUrl: String?,
    val receiveNewsletter: Boolean,
    val subscribedFrom: String,
)
