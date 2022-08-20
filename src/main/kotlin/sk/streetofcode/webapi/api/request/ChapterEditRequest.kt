package sk.streetofcode.webapi.api.request

data class ChapterEditRequest(
    val id: Long,
    val name: String,
    val chapterOrder: Int
)
