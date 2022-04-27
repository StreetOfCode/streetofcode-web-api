package sk.streetofcode.courseplatformbackend.api.request

data class CourseReviewAddRequest(
    val courseId: Long,
    val rating: Double,
    val text: String?,
    val userName: String?
)
