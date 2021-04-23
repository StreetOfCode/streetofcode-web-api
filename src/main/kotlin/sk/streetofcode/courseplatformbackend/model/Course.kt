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
    @JoinColumn(name = "author_id", nullable = true)
    var author: Author? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_id", nullable = true)
    var difficulty: Difficulty? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var shortDescription: String,

    @Column(nullable = false)
    var longDescription: String,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var status: CourseStatus,

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
    var updatedAt: OffsetDateTime

) {
    constructor(author: Author, difficulty: Difficulty, name: String, shortDescription: String, longDescription: String, imageUrl: String?, status: CourseStatus) :
        this(null, author, difficulty, name, shortDescription, longDescription, imageUrl, status, mutableSetOf(), OffsetDateTime.now(), OffsetDateTime.now())

    override fun equals(other: Any?) = other is Course && CourseEssential(this) == CourseEssential(other)
    override fun hashCode() = CourseEssential(this).hashCode()
    override fun toString() = CourseEssential(this).toString().replaceFirst("CourseEssential", "Course")
}

/***
 * DRAFT - course in process of creation
 * PRIVATE - course ready to view and test privately, not ready for production
 * PUBLIC - course ready for production. Only these courses should be visible to users
 */
enum class CourseStatus {
    DRAFT, PRIVATE, PUBLIC
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
