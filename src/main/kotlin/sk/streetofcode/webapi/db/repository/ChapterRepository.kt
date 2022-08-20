package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.Chapter
import javax.transaction.Transactional

@Repository
interface ChapterRepository : CrudRepository<Chapter, Long> {
    fun findByCourseId(courseId: Long): List<Chapter>

    @Transactional
    fun deleteByCourseId(courseId: Long)
}
