package sk.streetofcode.webapi.api.request

data class CourseReviewEditRequest(
    val rating: Double,
    val text: String?,
)
