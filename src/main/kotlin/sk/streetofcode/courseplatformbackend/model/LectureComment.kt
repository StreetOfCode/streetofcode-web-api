package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.LectureCommentDto
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class LectureComment(
    @Id
    @SequenceGenerator(name = "lecture_comment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_comment_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_firebase_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    val lecture: Lecture,

    @Column(nullable = false, columnDefinition = "TEXT")
    var commentText: String,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: OffsetDateTime
) {
    constructor(user: User, lecture: Lecture, commentText: String) :
        this(null, user, lecture, commentText, OffsetDateTime.now(), OffsetDateTime.now())

    override fun equals(other: Any?) =
        other is LectureComment && LectureCommentEssential(this) == LectureCommentEssential(other)

    override fun hashCode() = LectureCommentEssential(this).hashCode()
    override fun toString() = LectureCommentEssential(this).toString().replaceFirst("LectureEssential", "Lecture")
}

private data class LectureCommentEssential(
    val userId: String,
    val userName: String,
    val commentText: String,
    val lectureId: Long,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime

) {
    constructor(lectureComment: LectureComment) : this(
        userId = lectureComment.user.firebaseId,
        userName = lectureComment.user.name,
        commentText = lectureComment.commentText,
        lectureId = lectureComment.lecture.id!!,
        createdAt = lectureComment.createdAt,
        updatedAt = lectureComment.updatedAt
    )
}

fun LectureComment.toLectureCommentDto(): LectureCommentDto {
    return LectureCommentDto(
        id = this.id!!,
        userId = this.user.firebaseId,
        userName = this.user.name,
        imageUrl = this.user.imageUrl,
        commentText = this.commentText,
        updatedAt = this.updatedAt
    )
}
