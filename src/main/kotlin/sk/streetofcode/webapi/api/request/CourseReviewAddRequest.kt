package sk.streetofcode.webapi.api.request

data class CourseReviewAddRequest(
    val courseId: Long,
    val rating: Double,
    val text: String?
)
