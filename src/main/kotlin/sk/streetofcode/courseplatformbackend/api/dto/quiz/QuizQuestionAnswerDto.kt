package sk.streetofcode.courseplatformbackend.api.dto.quiz

data class QuizQuestionAnswerDto(
    val id: Long,
    val question: QuizQuestionDto,
    val text: String
)

data class QuizQuestionAnswerCorrectnessDto(
    val isCorrect: Boolean
)