package sk.streetofcode.webapi.api.request

data class QuizQuestionUserAnswerRequest(
    val questionId: Long,
    val answerIds: List<Long>,
)
