package sk.streetofcode.courseplatformbackend.api.dto

import sk.streetofcode.courseplatformbackend.model.Difficulty

data class AuthorOverviewDto(
    val id: Long,
    val name: String,
    val url: String,
    val description: String,
    val courses: List<AuthorCourseOverviewDto>
)

data class AuthorCourseOverviewDto(
    val id: Long,
    val difficulty: Difficulty? = null,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val imageUrl: String? = null,
    var reviewsOverview: CourseReviewsOverviewDto
)
