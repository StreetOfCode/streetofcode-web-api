package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewDto
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class CourseReview(
    @Id
    @SequenceGenerator(name = "course_review_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_review_id_seq")
    val id: Long? = null,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    var courseId: Long,

    @Column(nullable = false)
    var rating: Double,

    @Column(nullable = true, columnDefinition = "TEXT")
    var text: String?,

    @Column(nullable = true)
    var userName: String?,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: OffsetDateTime
) {
    constructor(userId: String, courseId: Long, rating: Double, text: String?, userName: String?) :
        this(null, userId, courseId, rating, text, userName, OffsetDateTime.now(), OffsetDateTime.now())

    override fun equals(other: Any?) = other is CourseReview && CourseReviewEssential(this) == CourseReviewEssential(other)
    override fun hashCode() = CourseReviewEssential(this).hashCode()
    override fun toString() = CourseReviewEssential(this).toString().replaceFirst("CourseReviewEssential", "CourseReview")
}

private data class CourseReviewEssential(
    val userId: String,
    val courseId: Long,
    val createdAt: OffsetDateTime
) {
    constructor(courseReview: CourseReview) : this(
        userId = courseReview.userId,
        courseId = courseReview.courseId,
        createdAt = courseReview.createdAt
    )
}

fun CourseReview.toCourseReviewDto(): CourseReviewDto {
    return CourseReviewDto(
        id = this.id!!,
        userId = this.userId,
        courseId = this.courseId,
        rating = this.rating,
        text = this.text,
        userName = this.userName
    )
}
