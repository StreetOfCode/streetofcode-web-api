package sk.streetofcode.courseplatformbackend.db.repository.vote

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVote

@Repository
interface NextCourseVoteRepository : CrudRepository<NextCourseVote, Long> {
    fun existsByUserId(userId: String): Boolean
}
