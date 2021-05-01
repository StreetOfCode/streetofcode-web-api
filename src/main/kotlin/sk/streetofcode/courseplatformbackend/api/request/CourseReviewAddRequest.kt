package sk.streetofcode.courseplatformbackend.api.request

data class CourseReviewAddRequest(
    val courseId: Long,
    val rating: Int,
    val text: String?,
    val userName: String?
)
