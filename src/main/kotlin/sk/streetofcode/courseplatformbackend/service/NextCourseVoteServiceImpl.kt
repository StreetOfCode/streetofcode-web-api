package sk.streetofcode.courseplatformbackend.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.NextCourseVoteService
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.PreconditionFailedException
import sk.streetofcode.courseplatformbackend.api.request.VoteNextCoursesRequest
import sk.streetofcode.courseplatformbackend.db.repository.vote.NextCourseVoteOptionRepository
import sk.streetofcode.courseplatformbackend.db.repository.vote.NextCourseVoteRepository
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVote
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVoteOption
import java.util.UUID

@Service
class NextCourseVoteServiceImpl(
    private val nextCourseVoteRepository: NextCourseVoteRepository,
    private val nextCourseOptionRepository: NextCourseVoteOptionRepository
) : NextCourseVoteService {

    companion object {
        private val log = LoggerFactory.getLogger(DifficultyServiceImpl::class.java)
    }

    override fun getOptions(userId: UUID?): List<NextCourseVoteOption> {
        if (userId != null && nextCourseVoteRepository.existsByUserId(userId)) {
            throw PreconditionFailedException("User has already voted")
        }

        return nextCourseOptionRepository.findAll().toList()
    }

    override fun addVote(userId: UUID?, voteRequest: VoteNextCoursesRequest) {
        try {
            nextCourseVoteRepository.saveAll(voteRequest.courseVoteOptionIds.map { NextCourseVote(userId, it) })
        } catch (e: Exception) {
            log.error("Problem with saving voteNextCourse to db", e)
            throw InternalErrorException("Could not save voteNextCourse")
        }
    }
}
