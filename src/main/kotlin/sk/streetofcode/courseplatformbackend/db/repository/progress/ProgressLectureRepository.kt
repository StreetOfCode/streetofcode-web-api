package sk.streetofcode.courseplatformbackend.db.repository.progress

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.progress.ProgressLecture
import java.util.Optional
import java.util.UUID
import javax.transaction.Transactional

@Repository
interface ProgressLectureRepository : CrudRepository<ProgressLecture, Long> {
    @Transactional
    fun deleteByUserIdAndLectureId(userId: UUID, lectureId: Long)
    @Transactional
    fun deleteByUserIdAndLectureIdIn(userId: UUID, lectureIds: List<Long>)
    fun findAllByUserId(userId: UUID): List<ProgressLecture>
    fun findByUserIdAndLectureId(userId: UUID, lectureId: Long): Optional<ProgressLecture>
}
