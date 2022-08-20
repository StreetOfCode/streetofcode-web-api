package sk.streetofcode.webapi.db.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.Course
import java.time.OffsetDateTime
import java.util.Optional
import javax.transaction.Transactional

@Repository
interface CourseRepository : CrudRepository<Course, Long> {

    fun findBySlug(slug: String): Optional<Course>

    @Modifying
    @Query("update Course c set updated_at = ?1, difficulty_id = null where difficulty_id = ?2")
    fun setDifficultyIdsToNull(updatedAt: OffsetDateTime = OffsetDateTime.now(), difficultyId: Long)

    @Modifying
    @Query("update Course c set updated_at = ?1, author_id = null where author_id = ?2")
    fun setAuthorsToNull(updatedAt: OffsetDateTime = OffsetDateTime.now(), authorId: Long)

    @Modifying
    @Transactional
    @Query("update Course c set lectures_count = lectures_count + (?1) where id = ?2")
    fun updateLecturesCount(delta: Int, courseId: Long)
}
