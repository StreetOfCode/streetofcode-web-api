package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.Course
import java.time.OffsetDateTime

@Repository
interface CourseRepository : CrudRepository<Course, Long> {

    @Modifying
    @Query("update Course c set updated_at = ?1, difficulty_id = null where difficulty_id = ?2")
    fun setDifficultyIdsToNull(updatedAt: OffsetDateTime = OffsetDateTime.now(), difficultyId: Long)

    @Modifying
    @Query("update Course c set updated_at = ?1, author_id = null where author_id = ?2")
    fun setAuthorsToNull(updatedAt: OffsetDateTime = OffsetDateTime.now(), authorId: Long)

    fun <T> findBy(projection: Class<T>): List<T>

    fun <T> findById(id: Long, projection: Class<T>): T
}