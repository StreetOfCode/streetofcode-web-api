package sk.streetofcode.courseplatformbackend.db.repository.progress

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.progress.UserProgressMetadata
import java.util.Optional
import java.util.UUID

@Repository
interface UserProgressMetadataRepository : CrudRepository<UserProgressMetadata, Long> {
    fun findByUserIdAndCourseId(userId: UUID, courseId: Long): Optional<UserProgressMetadata>
}
