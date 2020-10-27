package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.Lecture
import javax.transaction.Transactional

@Repository
interface LectureRepository : CrudRepository<Lecture, Long> {

    @Transactional
    fun deleteByChapterId(chapterId: Long)

    fun findByChapterId(chapterId: Long): List<Lecture>
}