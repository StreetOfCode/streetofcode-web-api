package sk.streetofcode.courseplatformbackend.api.request

data class QuizQuestionAnswerEditRequest(
    val id: Long,
    val questionId: Long,
    val text: String,
    val isCorrect: Boolean
)
