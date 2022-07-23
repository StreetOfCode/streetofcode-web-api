package sk.streetofcode.courseplatformbackend.api.dto

data class AuthorOverviewDto(
    val id: Long,
    val name: String,
    val slug: String,
    val imageUrl: String,
    val coursesTitle: String,
    val email: String,
    val description: String,
    val courses: List<CourseOverviewDto>
)
