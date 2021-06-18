package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.courseplatformbackend.model.progress.UserProgressMetadata

@Component
class ProgressMapper {
    fun toUserProgressMetadataDto(
        userProgressMetadata: UserProgressMetadata,
        courseLecturesCount: Int,
        nextChapterId: Long?,
        nextLectureId: Long?
    ): UserProgressMetadataDto {
        return UserProgressMetadataDto(
            lecturesViewed = userProgressMetadata.lecturesViewed,
            courseLecturesCount = courseLecturesCount,
            status = userProgressMetadata.status,
            startedAt = userProgressMetadata.startedAt,
            lastUpdatedAt = userProgressMetadata.lastUpdatedAt,
            finishedAt = userProgressMetadata.finishedAt,
            nextChapterId = nextChapterId,
            nextLectureId = nextLectureId
        )
    }
}
