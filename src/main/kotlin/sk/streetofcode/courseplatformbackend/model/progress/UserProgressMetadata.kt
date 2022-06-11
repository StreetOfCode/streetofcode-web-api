package sk.streetofcode.courseplatformbackend.model.progress

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.progress.UserProgressMetadataDto
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class UserProgressMetadata(
    @Id
    @SequenceGenerator(name = "user_progress_metadata_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_progress_metadata_id_seq")
    val id: Long? = null,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    val courseId: Long,

    @Column(nullable = false)
    var lecturesViewed: Int,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var status: ProgressStatus,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val startedAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var lastUpdatedAt: OffsetDateTime,

    @Column(nullable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var finishedAt: OffsetDateTime? = null
) {
    constructor(userId: String, courseId: Long, lecturesViewed: Int) :
        this(null, userId, courseId, lecturesViewed, ProgressStatus.IN_PROGRESS, OffsetDateTime.now(), OffsetDateTime.now(), null)

    override fun equals(other: Any?) =
        other is UserProgressMetadata && UserProgressMetadataEssential(this) == UserProgressMetadataEssential(other)

    override fun hashCode() = UserProgressMetadataEssential(this).hashCode()
    override fun toString() = UserProgressMetadataEssential(this).toString()
        .replaceFirst("UserProgressMetadataEssential", "UserProgressMetadata")
}

enum class ProgressStatus {
    IN_PROGRESS, FINISHED
}

private data class UserProgressMetadataEssential(
    val userId: String,
    val courseId: Long,
    val lecturesViewed: Int,
    val status: ProgressStatus,
    val startedAt: OffsetDateTime,
    val lastUpdatedAt: OffsetDateTime,
    val finishedAt: OffsetDateTime?

) {
    constructor(userProgressMetadata: UserProgressMetadata) : this(
        userId = userProgressMetadata.userId,
        courseId = userProgressMetadata.courseId,
        lecturesViewed = userProgressMetadata.lecturesViewed,
        status = userProgressMetadata.status,
        startedAt = userProgressMetadata.startedAt,
        lastUpdatedAt = userProgressMetadata.lastUpdatedAt,
        finishedAt = userProgressMetadata.finishedAt
    )
}

fun UserProgressMetadata.toUserProgressMetadataDto(
    courseLecturesCount: Int,
    nextChapterId: Long?,
    nextLectureId: Long?
): UserProgressMetadataDto {
    return UserProgressMetadataDto(
        lecturesViewed = this.lecturesViewed,
        courseLecturesCount = courseLecturesCount,
        status = this.status,
        startedAt = this.startedAt,
        lastUpdatedAt = this.lastUpdatedAt,
        finishedAt = this.finishedAt,
        nextChapterId = nextChapterId,
        nextLectureId = nextLectureId
    )
}
