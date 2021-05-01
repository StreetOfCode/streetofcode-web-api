package sk.streetofcode.courseplatformbackend.api.dto

data class CourseReviewDto(
    val id: Long,
    val courseId: Long,
    val rating: Int,
    val text: String?,
    val userName: String?
)

data class CourseReviewsOverviewDto(
    val averageRating: Double,
    val numberOfReviews: Long
)
