package sk.streetofcode.webapi.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.webapi.api.dto.ChapterOverviewDto
import sk.streetofcode.webapi.api.dto.CourseDto
import sk.streetofcode.webapi.api.dto.CourseOverviewDto
import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.webapi.api.dto.LectureOverviewDto
import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.service.LectureServiceImpl
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
data class Course(
    @Id
    @SequenceGenerator(name = "course_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    var author: Author,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_id")
    var difficulty: Difficulty,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var slug: String,

    @Column(nullable = false)
    var shortDescription: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var longDescription: String,

    /***
     * Course can have resources, which is a readme text containing links and stuff
     */
    @Column(nullable = true, columnDefinition = "TEXT")
    var resources: String? = null,

    @Column(nullable = true)
    var trailerUrl: String? = null,

    @Column(nullable = true)
    var thumbnailUrl: String? = null,

    @Column(nullable = false)
    var iconUrl: String,

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
    var updatedAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "integer default 0")
    var lecturesCount: Int,

    @Column(nullable = false, columnDefinition = "integer default 0")
    var courseOrder: Int
) {
    constructor(
        author: Author,
        difficulty: Difficulty,
        name: String,
        slug: String,
        shortDescription: String,
        longDescription: String,
        resources: String?,
        trailerUrl: String?,
        thumbnailUrl: String?,
        iconUrl: String,
        status: CourseStatus,
        courseOrder: Int
    ) :
        this(
            null,
            author,
            difficulty,
            name,
            slug,
            shortDescription,
            longDescription,
            resources,
            trailerUrl,
            thumbnailUrl,
            iconUrl,
            status,
            mutableSetOf(),
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            0,
            courseOrder
        )

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
    val slug: String,
    val shortDescription: String,
    val longDescription: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime

) {
    constructor(course: Course) : this(
        name = course.name,
        slug = course.slug,
        shortDescription = course.shortDescription,
        longDescription = course.longDescription,
        createdAt = course.createdAt,
        updatedAt = course.updatedAt
    )
}

fun Course.toCourseDto(): CourseDto {
    return CourseDto(
        this.id!!,
        this.author,
        this.difficulty,
        this.name,
        this.slug,
        this.shortDescription,
        this.longDescription,
        this.resources,
        this.trailerUrl,
        this.thumbnailUrl,
        this.iconUrl,
        this.status,
        this.chapters.map { it.toCourseChapterDto() }.toSet(),
        this.createdAt.truncatedTo(ChronoUnit.SECONDS),
        this.updatedAt.truncatedTo(ChronoUnit.SECONDS),
        this.lecturesCount,
        this.courseOrder
    )
}

fun Course.toCourseOverview(
    courseReviewsOverview: CourseReviewsOverviewDto,
    userProgressMetadata: UserProgressMetadataDto?,
    courseProducts: List<CourseProductDto>
): CourseOverviewDto {

    val chapters = this.chapters.map { chapter ->
        ChapterOverviewDto(
            chapter.id!!,
            chapter.name,
            chapter.lectures.map { lecture ->
                LectureOverviewDto(
                    lecture.id!!,
                    lecture.name,
                    lecture.videoDurationSeconds,
                    LectureServiceImpl.determineLectureType(lecture)
                )
            },
            chapterDurationMinutes = chapter.lectures.sumOf { lecture ->
                TimeUnit.SECONDS.toMinutes(lecture.videoDurationSeconds.toLong()).toInt()
            }
        )
    }.toSet()

    return CourseOverviewDto(
        this.id!!,
        this.name,
        this.slug,
        this.courseOrder,
        this.shortDescription,
        this.longDescription,
        this.resources,
        this.trailerUrl,
        this.thumbnailUrl,
        this.iconUrl,
        this.status,
        this.author,
        this.difficulty,
        this.createdAt.truncatedTo(ChronoUnit.SECONDS),
        this.updatedAt.truncatedTo(ChronoUnit.SECONDS),
        chapters,
        courseDurationMinutes = chapters.sumOf { chapter -> chapter.chapterDurationMinutes },
        reviewsOverview = courseReviewsOverview,
        userProgressMetadata = userProgressMetadata,
        courseProducts,
    )
}
