package sk.streetofcode.webapi.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.webapi.api.dto.ChapterCourseDto
import sk.streetofcode.webapi.api.dto.ChapterDto
import sk.streetofcode.webapi.api.dto.CourseChapterDto
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
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Chapter(
    @Id
    @SequenceGenerator(name = "chapter_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    val course: Course,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var chapterOrder: Int,

    @OneToMany(
        mappedBy = "chapter",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @OrderBy("lecture_order")
    val lectures: MutableSet<Lecture>,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: OffsetDateTime
) {
    constructor(course: Course, name: String, chapterOrder: Int) :
            this(null, course, name, chapterOrder, mutableSetOf(), OffsetDateTime.now(), OffsetDateTime.now())

    override fun equals(other: Any?) = other is Chapter && ChapterEssential(this) == ChapterEssential(other)
    override fun hashCode() = ChapterEssential(this).hashCode()
    override fun toString() = ChapterEssential(this).toString().replaceFirst("ChapterEssential", "Chapter")
}

private data class ChapterEssential(
    val name: String,
    val chapterOrder: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) {
    constructor(chapter: Chapter) : this(
        name = chapter.name,
        chapterOrder = chapter.chapterOrder,
        createdAt = chapter.createdAt,
        updatedAt = chapter.updatedAt
    )
}

fun Chapter.toChapterDto(): ChapterDto {
    return ChapterDto(
        this.id!!,
        ChapterCourseDto(this.course.id!!, this.course.name, this.course.lecturesCount),
        this.name,
        this.chapterOrder,
        this.lectures.map { it.toLectureDto() }.toSet(),
        this.createdAt.truncatedTo(ChronoUnit.SECONDS),
        this.updatedAt.truncatedTo(ChronoUnit.SECONDS)
    )
}

fun Chapter.toCourseChapterDto(): CourseChapterDto {
    return CourseChapterDto(
        this.id!!,
        this.name
    )
}
