package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.VoteNextCoursesRequest
import sk.streetofcode.webapi.model.vote.NextCourseVoteOption

interface NextCourseVoteService {
    fun getOptions(userId: String? = null): List<NextCourseVoteOption>
    fun addVote(userId: String? = null, voteRequest: VoteNextCoursesRequest)
}
