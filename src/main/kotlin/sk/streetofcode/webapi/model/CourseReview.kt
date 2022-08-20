package sk.streetofcode.webapi.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.webapi.api.dto.CourseReviewDto
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
data class CourseReview(
    @Id
    @SequenceGenerator(name = "course_review_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_review_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "soc_user_firebase_id", nullable = false)
    val socUser: SocUser,

    @Column(nullable = false)
    var courseId: Long,

    @Column(nullable = false)
    var rating: Double,

    @Column(nullable = true, columnDefinition = "TEXT")
    var text: String?,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: OffsetDateTime
) {
    constructor(socUser: SocUser, courseId: Long, rating: Double, text: String?) :
        this(null, socUser, courseId, rating, text, OffsetDateTime.now(), OffsetDateTime.now())

    override fun equals(other: Any?) = other is CourseReview && CourseReviewEssential(this) == CourseReviewEssential(other)
    override fun hashCode() = CourseReviewEssential(this).hashCode()
    override fun toString() = CourseReviewEssential(this).toString().replaceFirst("CourseReviewEssential", "CourseReview")
}

private data class CourseReviewEssential(
    val userId: String,
    val courseId: Long,
    val userName: String,
    val createdAt: OffsetDateTime
) {
    constructor(courseReview: CourseReview) : this(
        userId = courseReview.socUser.firebaseId,
        courseId = courseReview.courseId,
        userName = courseReview.socUser.name,
        createdAt = courseReview.createdAt
    )
}

fun CourseReview.toCourseReviewDto(): CourseReviewDto {
    return CourseReviewDto(
        id = this.id!!,
        userId = this.socUser.firebaseId,
        courseId = this.courseId,
        rating = this.rating,
        text = this.text,
        userName = this.socUser.name,
        imageUrl = this.socUser.imageUrl
    )
}
