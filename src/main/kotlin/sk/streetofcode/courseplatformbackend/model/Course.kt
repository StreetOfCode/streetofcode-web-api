package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Course(
        @Id
        @SequenceGenerator(name = "course_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_id_seq")
        val id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "author_id", nullable = false)
        val author: Author,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "difficulty_id", nullable = false)
        val difficulty: Difficulty,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val shortDescription: String,

        @Column(nullable = false)
        val longDescription: String,

        @OneToMany(
                mappedBy = "course",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY
        )
        @OrderBy("chapter_order")
        val chapters: MutableSet<Chapter>,

        @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val createdAt: OffsetDateTime,

        @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val updatedAt: OffsetDateTime

) {
    constructor(author: Author, difficulty: Difficulty, name: String, shortDescription: String, longDescription: String)
            : this(null, author, difficulty, name, shortDescription, longDescription, mutableSetOf(),OffsetDateTime.now(), OffsetDateTime.now())

        override fun equals(other: Any?) = other is Course && CourseEssential(this) == CourseEssential(other)
        override fun hashCode() = CourseEssential(this).hashCode()
        override fun toString() = CourseEssential(this).toString().replaceFirst("CourseEssential", "Course")

}

private data class CourseEssential(
        val name: String,
        val shortDescription: String,
        val longDescription: String,
        val createdAt: OffsetDateTime,
        val updatedAt: OffsetDateTime

) {
        constructor(course: Course) : this(
                name = course.name,
                shortDescription = course.shortDescription,
                longDescription = course.longDescription,
                createdAt = course.createdAt,
                updatedAt = course.updatedAt
        )
}