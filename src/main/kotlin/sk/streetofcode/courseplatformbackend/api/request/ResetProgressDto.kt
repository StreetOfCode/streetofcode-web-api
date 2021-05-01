package sk.streetofcode.courseplatformbackend.api.request

data class ResetProgressDto(
    val courseId: Long? = null,
    val chapterId: Long? = null,
    val lectureId: Long? = null
)
