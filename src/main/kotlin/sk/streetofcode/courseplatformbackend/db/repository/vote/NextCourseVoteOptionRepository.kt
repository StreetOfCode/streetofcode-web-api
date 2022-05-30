package sk.streetofcode.courseplatformbackend.db.repository.vote

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVoteOption

@Repository
interface NextCourseVoteOptionRepository : CrudRepository<NextCourseVoteOption, Long>
