package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.LectureComment

@Repository
interface LectureCommentRepository : CrudRepository<LectureComment, Long> {
    fun findAllByLectureId(lectureId: Long): List<LectureComment>
}
