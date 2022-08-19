package sk.streetofcode.courseplatformbackend.api.request

data class AddEmailToNewsletterRequest(
    val email: String,
    val recaptchaToken: String?
)
