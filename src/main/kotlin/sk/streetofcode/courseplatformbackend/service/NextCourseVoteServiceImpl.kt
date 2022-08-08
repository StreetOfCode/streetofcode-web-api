package sk.streetofcode.courseplatformbackend.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.NextCourseVoteService
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.PreconditionFailedException
import sk.streetofcode.courseplatformbackend.api.request.VoteNextCoursesRequest
import sk.streetofcode.courseplatformbackend.client.recaptcha.RecaptchaApiClient
import sk.streetofcode.courseplatformbackend.db.repository.vote.NextCourseVoteOptionRepository
import sk.streetofcode.courseplatformbackend.db.repository.vote.NextCourseVoteRepository
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVote
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVoteOption

@Service
class NextCourseVoteServiceImpl(
    private val nextCourseVoteRepository: NextCourseVoteRepository,
    private val nextCourseOptionRepository: NextCourseVoteOptionRepository,
    private val recaptchaApiClient: RecaptchaApiClient
) : NextCourseVoteService {

    companion object {
        private val log = LoggerFactory.getLogger(DifficultyServiceImpl::class.java)
    }

    override fun getOptions(userId: String?): List<NextCourseVoteOption> {
        if (userId != null && nextCourseVoteRepository.existsByUserId(userId)) {
            throw PreconditionFailedException("User has already voted")
        }

        return nextCourseOptionRepository.findAll().toList().filter { it.disabled == null || !it.disabled }
    }

    override fun addVote(userId: String?, voteRequest: VoteNextCoursesRequest) {
        if (userId == null) {
            if (voteRequest.recaptchaToken == null) {
                log.warn("Anonymous add vote request without recaptchaToken")
                return
            }

            if (!recaptchaApiClient.verifyRecaptchaToken(voteRequest.recaptchaToken)) {
                log.warn("Anonymous add vote request with failed verification of recaptcha token")
                return
            }
        }
        try {
            nextCourseVoteRepository.saveAll(voteRequest.courseVoteOptionIds.map { NextCourseVote(userId, it) })
        } catch (e: Exception) {
            log.error("Problem with saving voteNextCourse to db", e)
            throw InternalErrorException("Could not save voteNextCourse")
        }
    }
}
