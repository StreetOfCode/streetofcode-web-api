package sk.streetofcode.courseplatformbackend.api.request

import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestionType

data class QuizQuestionEditRequest(
    val id: Long,
    val quizId: Long,
    val questionOrder: Int,
    val text: String,
    val type: QuizQuestionType
)
