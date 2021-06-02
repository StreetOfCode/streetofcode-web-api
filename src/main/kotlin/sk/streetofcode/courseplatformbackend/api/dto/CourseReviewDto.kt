package sk.streetofcode.courseplatformbackend.api.dto

import java.util.*

data class CourseReviewDto(
    val id: Long,
    val userId: UUID,
    val courseId: Long,
    val rating: Int,
    val text: String?,
    val userName: String?
)

data class CourseReviewsOverviewDto(
    val averageRating: Double,
    val numberOfReviews: Long
)
