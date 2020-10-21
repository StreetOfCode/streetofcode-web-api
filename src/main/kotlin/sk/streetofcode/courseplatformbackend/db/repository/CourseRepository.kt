package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.Course
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Repository
interface CourseRepository : CrudRepository<Course, Long>{
    @Transactional
    fun deleteByAuthorId(authorId: Long)

    @Modifying
    @Query("update Course c set updated_at = ?1, difficulty_id = null where difficulty_id = ?2")
    fun setDifficultyIdsToNull(updatedAt: OffsetDateTime = OffsetDateTime.now(), difficultyId: Long)

    fun <T> findBy(projection: Class<T>): List<T>

    fun <T> findById(id: Long, projection: Class<T>): T
}