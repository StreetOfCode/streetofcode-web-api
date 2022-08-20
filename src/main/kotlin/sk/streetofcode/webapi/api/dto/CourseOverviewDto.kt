package sk.streetofcode.webapi.api.dto

import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.model.Author
import sk.streetofcode.webapi.model.CourseStatus
import sk.streetofcode.webapi.model.Difficulty
import java.time.OffsetDateTime

data class CourseOverviewDto(
    val id: Long,
    val name: String,
    val slug: String,
    val shortDescription: String,
    val longDescription: String,
    val resources: String? = null,
    val trailerUrl: String? = null,
    val thumbnailUrl: String? = null,
    val iconUrl: String,
    val status: CourseStatus,
    val author: Author,
    val difficulty: Difficulty,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val chapters: Set<ChapterOverviewDto>,
    val courseDurationMinutes: Int,
    val reviewsOverview: CourseReviewsOverviewDto,
    val userProgressMetadata: UserProgressMetadataDto?
)

data class ChapterOverviewDto(
    val id: Long,
    val name: String,
    val lectures: List<LectureOverviewDto>,
    val chapterDurationMinutes: Int
)

data class LectureOverviewDto(
    val id: Long,
    val name: String,
    val videoDurationSeconds: Int,
    val lectureType: LectureType
)
