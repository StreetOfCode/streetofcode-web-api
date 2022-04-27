package sk.streetofcode.courseplatformbackend.api.dto

import java.util.UUID

data class CourseReviewDto(
    val id: Long,
    val userId: UUID,
    val courseId: Long,
    val rating: Double,
    val text: String?,
    val userName: String?
)

data class CourseReviewsOverviewDto(
    val averageRating: Double,
    val numberOfReviews: Long
)
