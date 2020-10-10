package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.Course
import javax.transaction.Transactional

@Repository
interface CourseRepository : CrudRepository<Course, Long>{
    @Transactional
    fun deleteByAuthorId(authorId: Long)

    @Transactional
    fun deleteByDifficultyId(difficultyId: Long)

    fun <T> findBy(projection: Class<T>): List<T>

    fun <T> findById(id: Long, projection: Class<T>): T
}