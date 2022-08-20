package sk.streetofcode.webapi.api.request

data class QuizAddRequest(
    val lectureId: Long,
    val title: String,
    val subtitle: String?,
    val finishedMessage: String?
)
