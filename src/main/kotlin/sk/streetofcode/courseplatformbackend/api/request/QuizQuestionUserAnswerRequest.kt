package sk.streetofcode.courseplatformbackend.api.request

data class QuizQuestionUserAnswerRequest(
    val questionId: Long,
    val answerIds: List<Long>,
)
