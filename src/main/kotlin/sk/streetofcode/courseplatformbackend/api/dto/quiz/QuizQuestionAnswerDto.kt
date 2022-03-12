package sk.streetofcode.courseplatformbackend.api.dto.quiz

import kotlin.Long

data class QuizQuestionAnswerDto(
    val id: Long,
    val questionId: Long,
    val text: String
)

data class QuizQuestionAnswerCorrectnessDto(
    val isCorrect: Boolean
)
