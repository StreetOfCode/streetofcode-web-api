package sk.streetofcode.webapi.api.dto.quiz

import sk.streetofcode.webapi.model.quiz.QuizQuestionType

data class QuizQuestionDto(
    val id: Long,
    val quiz: QuizDto,
    val questionOrder: Int,
    val text: String,
    val type: QuizQuestionType,
    val answers: Set<QuizQuestionAnswerDto>
)
