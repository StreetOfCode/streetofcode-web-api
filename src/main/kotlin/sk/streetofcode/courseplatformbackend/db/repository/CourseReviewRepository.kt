package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.CourseReview
import java.util.*

interface CourseReviewsOverviewProjection {
    val averageRating: Double?
    val numberOfRatings: Long?
}

@Repository
interface CourseReviewRepository : CrudRepository<CourseReview, Long> {
    fun findByCourseId(courseId: Long): List<CourseReview>
    fun findByUserIdAndCourseId(userId: UUID, courseId: Long): CourseReview?

    @Query("SELECT AVG(cr.rating) as averageRating, COUNT(*) as numberOfRatings FROM CourseReview cr WHERE cr.courseId = ?1")
    fun getCourseReviewsOverview(courseId: Long): CourseReviewsOverviewProjection
}
