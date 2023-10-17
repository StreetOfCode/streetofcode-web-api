package sk.streetofcode.webapi.api.request

data class LectureAddRequest(
    val chapterId: Long,
    val name: String,
    val lectureOrder: Int,
    val content: String? = null,
    val videoUrl: String? = null,
    val allowPreviewWhenPaid: Boolean,
)
