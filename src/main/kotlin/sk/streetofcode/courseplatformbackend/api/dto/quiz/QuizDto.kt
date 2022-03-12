package sk.streetofcode.courseplatformbackend.api.dto.quiz

import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import java.time.OffsetDateTime

data class QuizDto(
    val id: Long,
    val lecture: LectureDto,
    val title: String,
    val subtitle: String?,
    val createdAt: OffsetDateTime,
    val finishedMessage: String?,
    val questions: Set<QuizQuestionDto>
)
