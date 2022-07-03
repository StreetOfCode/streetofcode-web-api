package sk.streetofcode.courseplatformbackend.api.request

data class CourseReviewEditRequest(
    val rating: Double,
    val text: String?,
)
