package sk.streetofcode.webapi.db.repository

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.Chapter
import javax.transaction.Transactional

@Repository
interface ChapterRepository : JpaRepository<Chapter, Long> {
    fun findByCourseId(courseId: Long, sort: Sort): List<Chapter>

    @Transactional
    fun deleteByCourseId(courseId: Long)
}
