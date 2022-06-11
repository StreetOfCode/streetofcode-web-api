package sk.streetofcode.courseplatformbackend.db.repository.progress

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.progress.UserProgressMetadata
import java.util.Optional

@Repository
interface UserProgressMetadataRepository : CrudRepository<UserProgressMetadata, Long> {
    fun findByUserIdAndCourseId(userId: String, courseId: Long): Optional<UserProgressMetadata>
    @Query(
        """
        SELECT progress.courseId from UserProgressMetadata as progress 
        WHERE progress.userId = :userId
        GROUP BY progress.courseId
        ORDER BY progress.lastUpdatedAt 
        """
    )
    fun getStartedCourseIds(userId: String): List<Long>
}
