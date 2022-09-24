package sk.streetofcode.webapi.db.repository

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.Lecture
import javax.transaction.Transactional

@Repository
interface LectureRepository : JpaRepository<Lecture, Long> {

    @Transactional
    fun deleteByChapterId(chapterId: Long)

    fun findByChapterId(chapterId: Long, sort: Sort): List<Lecture>
}
