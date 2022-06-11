package sk.streetofcode.courseplatformbackend.api.dto

import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.CourseStatus
import sk.streetofcode.courseplatformbackend.model.Difficulty
import java.time.OffsetDateTime

data class CourseDto(
    val id: Long,
    val author: Author,
    val difficulty: Difficulty,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val resources: String? = null,
    val trailerUrl: String? = null,
    val thumbnailUrl: String? = null,
    val iconUrl: String,
    val status: CourseStatus,
    val chapters: Set<CourseChapterDto>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val lecturesCount: Int
)

data class CourseChapterDto(
    val id: Long,
    val name: String
)
