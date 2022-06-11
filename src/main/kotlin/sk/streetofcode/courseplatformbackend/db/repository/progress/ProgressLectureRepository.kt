package sk.streetofcode.courseplatformbackend.db.repository.progress

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.progress.ProgressLecture
import java.util.Optional
import javax.transaction.Transactional

@Repository
interface ProgressLectureRepository : CrudRepository<ProgressLecture, Long> {
    @Transactional
    fun deleteByUserIdAndLectureId(userId: String, lectureId: Long)
    @Transactional
    fun deleteByUserIdAndLectureIdIn(userId: String, lectureIds: List<Long>)
    fun findAllByUserId(userId: String): List<ProgressLecture>
    fun findByUserIdAndLectureId(userId: String, lectureId: Long): Optional<ProgressLecture>
}
