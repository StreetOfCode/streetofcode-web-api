package sk.streetofcode.courseplatformbackend.api.request

data class QuizQuestionAnswerAddRequest(
    val questionId: Long,
    val text: String,
    val isCorrect: Boolean
)
