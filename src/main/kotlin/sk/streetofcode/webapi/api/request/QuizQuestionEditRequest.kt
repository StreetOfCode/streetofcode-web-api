package sk.streetofcode.webapi.api.request

import sk.streetofcode.webapi.model.quiz.QuizQuestionType

data class QuizQuestionEditRequest(
    val id: Long,
    val quizId: Long,
    val questionOrder: Int,
    val text: String,
    val type: QuizQuestionType
)
