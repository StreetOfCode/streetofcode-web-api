package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.Chapter
import javax.transaction.Transactional

@Repository
interface ChapterRepository : CrudRepository<Chapter, Long> {
    fun findByCourseId(courseId: Long): List<Chapter>

    @Transactional // TODO not sure how to use this
    fun deleteByCourseId(courseId: Long)
}