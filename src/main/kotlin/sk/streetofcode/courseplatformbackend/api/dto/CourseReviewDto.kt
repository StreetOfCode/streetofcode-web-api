package sk.streetofcode.courseplatformbackend.api.dto

data class CourseReviewDto(
    val id: Long,
    val userId: String,
    val courseId: Long,
    val rating: Double,
    val text: String?,
    val userName: String,
    val imageUrl: String?
)

data class CourseReviewsOverviewDto(
    val averageRating: Double,
    val numberOfReviews: Long
)
