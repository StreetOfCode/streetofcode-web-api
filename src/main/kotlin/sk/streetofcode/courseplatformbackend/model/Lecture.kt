package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.LectureChapterDto
import sk.streetofcode.courseplatformbackend.api.dto.LectureCourseDto
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.model.quiz.Quiz
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Lecture(
    @Id
    @SequenceGenerator(name = "lecture_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    val chapter: Chapter,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var lectureOrder: Int,

    @Column(nullable = true, columnDefinition = "TEXT")
    var content: String? = null,

    @Column(nullable = true, columnDefinition = "TEXT")
    var videoUrl: String? = null,

    @Column(nullable = false, columnDefinition = "integer default 0")
    var videoDurationSeconds: Int,

    @OneToMany(
        mappedBy = "lecture",
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY
    )
    val comments: MutableSet<LectureComment>,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: OffsetDateTime,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = true)
    var quiz: Quiz? = null
) {
    constructor(
        chapter: Chapter,
        name: String,
        lectureOrder: Int,
        content: String?,
        videoUrl: String?,
        videoDurationSeconds: Int
    ) :
        this(
            null,
            chapter,
            name,
            lectureOrder,
            content,
            videoUrl,
            videoDurationSeconds,
            mutableSetOf(),
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            null
        )

    override fun equals(other: Any?) = other is Lecture && LectureEssential(this) == LectureEssential(other)
    override fun hashCode() = LectureEssential(this).hashCode()
    override fun toString() = LectureEssential(this).toString().replaceFirst("LectureEssential", "Lecture")
}

private data class LectureEssential(
    val name: String,
    val lectureOrder: Int,
    val content: String?,
    val videoUrl: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime

) {
    constructor(lecture: Lecture) : this(
        name = lecture.name,
        lectureOrder = lecture.lectureOrder,
        content = lecture.content,
        videoUrl = lecture.videoUrl,
        createdAt = lecture.createdAt,
        updatedAt = lecture.updatedAt
    )
}

fun Lecture.toLectureDto(): LectureDto {
    return LectureDto(
        this.id!!,
        LectureChapterDto(this.chapter.id!!, this.chapter.name),
        LectureCourseDto(this.chapter.course.id!!, this.chapter.course.lecturesCount),
        this.name,
        this.lectureOrder,
        this.content,
        this.videoUrl,
        this.videoDurationSeconds,
        this.createdAt.truncatedTo(ChronoUnit.SECONDS),
        this.updatedAt.truncatedTo(ChronoUnit.SECONDS)
    )
}
