package sk.streetofcode.courseplatformbackend.api.dto

import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Difficulty
import java.time.OffsetDateTime

data class CourseOverviewDto(
        val id: Long,
        val name: String,
        val shortDescription: String,
        val longDescription: String,
        val imageUrl: String? = null,
        val author: Author? = null,
        val difficulty: Difficulty? = null,
        val createdAt: OffsetDateTime,
        val updatedAt: OffsetDateTime,
        val chapters: Set<ChapterOverviewDto>
)

data class ChapterOverviewDto(
        val id: Long,
        val name: String,
        val lectures: List<LectureOverviewDto>
)

data class LectureOverviewDto(
        val id: Long,
        val name: String
)