package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.LectureComment

@Repository
interface LectureCommentRepository : CrudRepository<LectureComment, Long> {
    fun findAllByLectureId(lectureId: Long): List<LectureComment>
}
