package sk.streetofcode.courseplatformbackend.db.repository.vote

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVote
import java.util.UUID

@Repository
interface NextCourseVoteRepository : CrudRepository<NextCourseVote, Long> {
    fun existsByUserId(userId: UUID): Boolean
}
