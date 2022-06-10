package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.AuthorCourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.AuthorOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewsOverviewDto
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Author(
    @Id
    @SequenceGenerator(name = "author_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val url: String,

    @Column(nullable = false)
    val description: String,

    @OneToMany(
        mappedBy = "author",
        cascade = [CascadeType.PERSIST],
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    @OrderBy("id")
    val courses: MutableSet<Course> = mutableSetOf()
) {
    constructor(id: Long, name: String, url: String, description: String) :
        this(id, name, url, description, mutableSetOf())

    constructor(name: String, url: String, description: String) :
        this(null, name, url, description, mutableSetOf())

    override fun equals(other: Any?) = other is Author && AuthorEssential(this) == AuthorEssential(other)
    override fun hashCode() = AuthorEssential(this).hashCode()
    override fun toString() = AuthorEssential(this).toString().replaceFirst("AuthorEssential", "Author")
}

private data class AuthorEssential(
    val name: String,
    val url: String,
    val description: String
) {
    constructor(author: Author) : this(
        name = author.name,
        url = author.url,
        description = author.description
    )
}

fun Author.toAuthorOverview(courseToCourseReviewOverview: List<Pair<Course, CourseReviewsOverviewDto>>): AuthorOverviewDto {
    return AuthorOverviewDto(
        this.id!!,
        this.name,
        this.url,
        this.description,
        courseToCourseReviewOverview.map { it.toAuthorCourseOverviewDto() }
    )
}

fun Pair<Course, CourseReviewsOverviewDto>.toAuthorCourseOverviewDto(): AuthorCourseOverviewDto {
    val (course, overview) = this
    return AuthorCourseOverviewDto(
        course.id!!,
        course.difficulty,
        course.name,
        course.shortDescription,
        course.longDescription,
        course.iconUrl,
        overview
    )
}
