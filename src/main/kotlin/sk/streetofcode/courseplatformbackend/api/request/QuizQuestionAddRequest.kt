package sk.streetofcode.courseplatformbackend.api.request

import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestionType

data class QuizQuestionAddRequest(
    val quizId: Long,
    val questionOrder: Int,
    val text: String,
    val type: QuizQuestionType
)
