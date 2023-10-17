package sk.streetofcode.webapi.api.request

data class LectureEditRequest(
    val id: Long,
    val name: String,
    val lectureOrder: Int,
    val allowPreviewWhenPaid: Boolean,
    val content: String? = null,
    val videoUrl: String? = null,
)
