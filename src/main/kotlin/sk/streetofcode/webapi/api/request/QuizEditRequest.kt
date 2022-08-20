package sk.streetofcode.webapi.api.request

data class QuizEditRequest(
    val id: Long,
    val lectureId: Long,
    val title: String,
    val subtitle: String?,
    val finishedMessage: String?
)
