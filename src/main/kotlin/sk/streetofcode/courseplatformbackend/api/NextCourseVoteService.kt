package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.VoteNextCoursesRequest
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVoteOption

interface NextCourseVoteService {
    fun getOptions(userId: String? = null): List<NextCourseVoteOption>
    fun addVote(userId: String? = null, voteRequest: VoteNextCoursesRequest)
}
