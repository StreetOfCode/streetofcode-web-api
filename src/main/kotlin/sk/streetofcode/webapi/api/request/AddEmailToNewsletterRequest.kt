package sk.streetofcode.webapi.api.request

data class AddEmailToNewsletterRequest(
    val email: String,
    val recaptchaToken: String?,
    val subscribedFrom: String
)
