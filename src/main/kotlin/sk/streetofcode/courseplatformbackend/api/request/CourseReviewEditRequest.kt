package sk.streetofcode.courseplatformbackend.api.request

data class CourseReviewEditRequest(
    val rating: Int,
    val text: String?,
    val userName: String?
)
