package sk.streetofcode.webapi.api.dto.quiz

import java.time.OffsetDateTime
import kotlin.Long

data class QuizDto(
    val id: Long,
    val lectureId: Long,
    val title: String,
    val subtitle: String?,
    val createdAt: OffsetDateTime,
    val finishedMessage: String?,
    val questionIds: Set<Long>
)
