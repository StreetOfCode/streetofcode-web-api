package sk.streetofcode.courseplatformbackend.api.dto

import sk.streetofcode.courseplatformbackend.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Difficulty

data class CourseMyDto(
    val id: Long,
    val name: String,
    val shortDescription: String,
    val author: Author,
    val difficulty: Difficulty,
    val iconUrl: String,
    val reviewsOverview: CourseReviewsOverviewDto,
    val userProgressMetadata: UserProgressMetadataDto
)
