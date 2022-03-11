package sk.streetofcode.courseplatformbackend.api.dto.quiz

data class QuizQuestionDto(
    val id: Long,
    val quiz: QuizDto,
    val questionOrder: Int,
    val text: String,
    val isMultipleChoice: Boolean,
    val answers: Set<QuizQuestionAnswerDto>
)
