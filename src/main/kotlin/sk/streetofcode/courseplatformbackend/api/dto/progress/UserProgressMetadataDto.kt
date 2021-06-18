package sk.streetofcode.courseplatformbackend.api.dto.progress

import sk.streetofcode.courseplatformbackend.model.progress.ProgressStatus
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
