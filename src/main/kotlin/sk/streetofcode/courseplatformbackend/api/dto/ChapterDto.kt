package sk.streetofcode.courseplatformbackend.api.dto

import java.time.OffsetDateTime

data class ChapterDto(
        val id: Long,
        val course: ChapterCourseDto,
        val name: String,
        val chapterOrder: Int,
        val lectures: Set<LectureDto>,
        val createdAt: OffsetDateTime,
        val updatedAt: OffsetDateTime
)

data class ChapterCourseDto(
        val id: Long,
        val name: String
)