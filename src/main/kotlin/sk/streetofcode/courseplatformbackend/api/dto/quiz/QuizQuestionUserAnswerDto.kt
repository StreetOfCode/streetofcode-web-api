package sk.streetofcode.courseplatformbackend.api.dto.quiz

import java.util.*
import kotlin.Long

data class QuizQuestionUserAnswerDto(
    val id: Long,
    val question: QuizQuestionDto,
    val answer: QuizQuestionAnswerDto,
    val userId: UUID

)
