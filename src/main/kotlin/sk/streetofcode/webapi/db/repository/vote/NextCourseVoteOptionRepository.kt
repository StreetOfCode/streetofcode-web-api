package sk.streetofcode.webapi.db.repository.vote

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.vote.NextCourseVoteOption

@Repository
interface NextCourseVoteOptionRepository : CrudRepository<NextCourseVoteOption, Long>
