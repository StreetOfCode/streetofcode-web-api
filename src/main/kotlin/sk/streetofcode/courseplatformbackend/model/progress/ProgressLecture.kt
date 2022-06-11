package sk.streetofcode.courseplatformbackend.model.progress

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class ProgressLecture(
    @Id
    @SequenceGenerator(name = "progress_lecture_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "progress_lecture_id_seq")
    val id: Long? = null,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    val lectureId: Long,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime
) {
    constructor(userId: String, lectureId: Long) :
        this(null, userId, lectureId, OffsetDateTime.now())

    override fun equals(other: Any?) = other is ProgressLecture && ProgressLectureEssential(this) == ProgressLectureEssential(other)
    override fun hashCode() = ProgressLectureEssential(this).hashCode()
    override fun toString() = ProgressLectureEssential(this).toString().replaceFirst("ProgressLectureEssential", "ProgressLecture")
}

private data class ProgressLectureEssential(
    val userId: String,
    val lectureId: Long,
    val createdAt: OffsetDateTime

) {
    constructor(progressLecture: ProgressLecture) : this(
        userId = progressLecture.userId,
        lectureId = progressLecture.lectureId,
        createdAt = progressLecture.createdAt
    )
}
