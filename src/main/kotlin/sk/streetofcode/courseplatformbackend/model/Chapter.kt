package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Chapter(
        @Id
        @SequenceGenerator(name = "chapter_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_id_seq")
        val id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "course_id", nullable = false)
        @JsonIgnore
        val course: Course,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val chapterOrder: Int,

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
        val updatedAt: OffsetDateTime
) {
    constructor(course: Course, name: String, chapterOrder: Int)
            : this(null, course, name, chapterOrder, mutableSetOf(), OffsetDateTime.now(), OffsetDateTime.now())

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