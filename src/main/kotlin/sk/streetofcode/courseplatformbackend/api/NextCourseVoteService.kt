package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.VoteNextCoursesRequest
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVoteOption
import java.util.UUID

interface NextCourseVoteService {
    fun getOptions(userId: UUID? = null): List<NextCourseVoteOption>
    fun addVote(userId: UUID? = null, voteRequest: VoteNextCoursesRequest)
}
