package sk.streetofcode.courseplatformbackend.api.dto

import java.time.OffsetDateTime

data class LectureDto(
    val id: Long,
    val chapter: LectureChapterDto,
    val course: LectureCourseDto,
    val name: String,
    val lectureOrder: Int,
    val content: String? = null,
    val videoUrl: String? = null,
    val videoDurationSeconds: Int? = null,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class LectureChapterDto(
    val id: Long,
    val name: String
)

data class LectureCourseDto(
    val id: Long,
    val lecturesCount: Int
)

enum class LectureType {
    VIDEO, TEXT, QUIZ
}
