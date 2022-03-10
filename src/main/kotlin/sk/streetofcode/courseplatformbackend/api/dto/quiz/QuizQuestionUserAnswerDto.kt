package sk.streetofcode.courseplatformbackend.api.dto.quiz

data class QuizQuestionUserAnswerDto(
    val id: Long,
    val question: QuizQuestionDto,
    val answer: QuizQuestionAnswerDto
)
