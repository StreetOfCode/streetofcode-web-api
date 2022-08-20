package sk.streetofcode.webapi.api.dto.quiz

data class QuizQuestionUserAnswerDto(
    val id: Long,
    val question: QuizQuestionDto,
    val answer: QuizQuestionAnswerDto,
    val userId: String,
    val tryCount: Int,
    var isCorrect: Boolean
)
