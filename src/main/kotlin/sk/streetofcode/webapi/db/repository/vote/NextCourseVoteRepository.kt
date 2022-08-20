package sk.streetofcode.webapi.db.repository.vote

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.vote.NextCourseVote

@Repository
interface NextCourseVoteRepository : CrudRepository<NextCourseVote, Long> {
    fun existsByUserId(userId: String): Boolean
}
