package sk.streetofcode.webapi.api.request

data class JavaCoursePromoCodeRequest(
    val email: String,
    val recaptchaToken: String?
)
