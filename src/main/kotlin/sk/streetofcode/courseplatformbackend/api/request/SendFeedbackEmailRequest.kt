package sk.streetofcode.courseplatformbackend.api.request

data class SendFeedbackEmailRequest(
    val email: String,
    val subject: String? = null,
    val emailText: String,
    val recaptchaToken: String?
)
