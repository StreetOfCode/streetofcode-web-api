package sk.streetofcode.courseplatformbackend.api.dto.quiz

import kotlin.Long

data class QuizQuestionAnswerDto(
    val id: Long,
    val questionId: Long,
    val text: String,
    val isCorrect: Boolean?
)

data class QuizQuestionAnswerCorrectnessDto(
    val isCorrect: Boolean
)
