package sk.streetofcode.webapi.api.dto.progress

import sk.streetofcode.webapi.model.progress.ProgressStatus
import java.time.OffsetDateTime

data class UserProgressMetadataDto(
    val lecturesViewed: Int,
    val courseLecturesCount: Int,
    val status: ProgressStatus,
    val startedAt: OffsetDateTime,
    val lastUpdatedAt: OffsetDateTime,
    val finishedAt: OffsetDateTime?,
    val nextChapterId: Long?,
    val nextLectureId: Long?
)
