package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Lecture(
        @Id
        @SequenceGenerator(name = "lecture_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_id_seq")
        val id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "chapter_id", nullable = false)
        @JsonIgnoreProperties("lectures", "name", "chapterOrder", "createdAt", "updatedAt")
        val chapter: Chapter,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var lectureOrder: Int,

        @Column(nullable = false, columnDefinition = "TEXT")
        var content: String,

        @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val createdAt: OffsetDateTime,

        @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
        var updatedAt: OffsetDateTime
) {
    constructor(chapter: Chapter, name: String, lectureOrder: Int, content: String)
            : this(null, chapter, name, lectureOrder, content, OffsetDateTime.now(), OffsetDateTime.now())

    override fun equals(other: Any?) = other is Lecture && LectureEssential(this) == LectureEssential(other)
        override fun hashCode() = LectureEssential(this).hashCode()
        override fun toString() = LectureEssential(this).toString().replaceFirst("LectureEssential", "Lecture")
}

private data class LectureEssential(
        val name: String,
        val lectureOrder: Int,
        val content: String,
        val createdAt: OffsetDateTime,
        val updatedAt: OffsetDateTime

) {
        constructor(lecture: Lecture) : this(
                name = lecture.name,
                lectureOrder = lecture.lectureOrder,
                content = lecture.content,
                createdAt = lecture.createdAt,
                updatedAt = lecture.updatedAt
        )
}