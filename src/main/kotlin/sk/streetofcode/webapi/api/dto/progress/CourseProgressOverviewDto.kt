package sk.streetofcode.webapi.api.dto.progress

import sk.streetofcode.webapi.api.dto.LectureType

data class CourseProgressOverviewDto(
    val lecturesViewed: Int,
    val courseLecturesCount: Int,
    val chapters: List<ChapterProgressOverviewDto>
)

data class ChapterProgressOverviewDto(
    val id: Long,
    val name: String,
    val viewed: Boolean,
    val chapterDurationMinutes: Int,
    val lectures: List<LectureProgressOverviewDto>
)

data class LectureProgressOverviewDto(
    val id: Long,
    val name: String,
    val viewed: Boolean,
    val videoDurationSeconds: Int,
    val lectureType: LectureType,
)
