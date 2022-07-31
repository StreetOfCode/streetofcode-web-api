package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.exception.PreconditionFailedException
import sk.streetofcode.courseplatformbackend.api.request.VoteNextCoursesRequest
import sk.streetofcode.courseplatformbackend.configuration.SpringBootTestAnnotation
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVoteOption

@SpringBootTestAnnotation
class VoteIntegrationTests : IntegrationTests() {
    init {
        "get next course options - not authenticated - OK" {
            val nextCourseOptionsResponse = getNextCourseOptionsNotAuthenticated()
            nextCourseOptionsResponse.statusCode shouldBe HttpStatus.OK
            nextCourseOptionsResponse.body!!.size shouldBe 7
        }

        "get next course options - authenticated - OK" {
            val nextCourseOptionsResponse = getNextCourseOptionsAuthenticated()
            nextCourseOptionsResponse.statusCode shouldBe HttpStatus.OK
            nextCourseOptionsResponse.body!!.size shouldBe 7
        }

        "vote next course scenario" {
            val nextCourseOptionsResponse = getNextCourseOptionsAuthenticated()
            nextCourseOptionsResponse.statusCode shouldBe HttpStatus.OK

            voteNextCourse(VoteNextCoursesRequest(listOf(1), recaptchaToken = null))

            getNextCourseOptionsPreconditionFailed()
        }
    }

    private fun getNextCourseOptionsAuthenticated(): ResponseEntity<List<NextCourseVoteOption>> {
        return restWithUserRole().getForEntity("/next-course-vote")
    }

    private fun getNextCourseOptionsNotAuthenticated(): ResponseEntity<List<NextCourseVoteOption>> {
        return restTemplate.getForEntity("/next-course-vote")
    }

    private fun voteNextCourse(body: VoteNextCoursesRequest) {
        return restWithUserRole().postForEntity<Void>("/next-course-vote", body).statusCode shouldBe HttpStatus.OK
    }

    private fun getNextCourseOptionsPreconditionFailed() {
        return restWithUserRole().getForEntity<PreconditionFailedException>("/next-course-vote")
            .statusCode shouldBe HttpStatus.PRECONDITION_FAILED
    }
}
