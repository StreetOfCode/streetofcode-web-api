package sk.streetofcode.courseplatformbackend.api.dto.progress

data class CourseProgressOverviewDto(
    val lecturesViewed: Int,
    val courseLecturesCount: Int,
    val chapters: List<ChapterProgressOverviewDto>
)

data class ChapterProgressOverviewDto(
    val id: Long,
    val name: String,
    val viewed: Boolean,
    val lectures: List<LectureProgressOverviewDto>
)

data class LectureProgressOverviewDto(
    val id: Long,
    val name: String,
    val viewed: Boolean
)