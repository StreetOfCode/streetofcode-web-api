package sk.streetofcode.courseplatformbackend.api.dto.quiz

import java.util.*

data class QuizQuestionUserAnswerDto(
    val id: Long,
    val question: QuizQuestionDto,
    val answer: QuizQuestionAnswerDto,
    val userId: String,
    val tryCount: Int,
    var isCorrect: Boolean
)
