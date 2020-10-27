package sk.streetofcode.courseplatformbackend.api.request

data class ChapterAddRequest(
        val courseId: Long,
        val name: String,
        val chapterOrder: Int
)