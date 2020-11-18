package sk.streetofcode.courseplatformbackend.api.dto

import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.CourseStatus
import sk.streetofcode.courseplatformbackend.model.Difficulty
import java.time.OffsetDateTime

data class CourseDto(
        val id: Long,
        val author: Author? = null,
        val difficulty: Difficulty? = null,
        val name: String,
        val shortDescription: String,
        val longDescription: String,
        val imageUrl: String? = null,
        val status: CourseStatus,
        val chapters: Set<CourseChapterDto>,
        val createdAt: OffsetDateTime,
        val updatedAt: OffsetDateTime
)

data class CourseChapterDto(
        val id: Long,
        val name: String
)