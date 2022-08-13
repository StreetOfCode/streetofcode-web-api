package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.courseplatformbackend.api.NextCourseVoteService
import sk.streetofcode.courseplatformbackend.api.request.VoteNextCoursesRequest
import sk.streetofcode.courseplatformbackend.model.vote.NextCourseVoteOption
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@RestController
@RequestMapping("next-course-vote")
class NextCourseVoteController(val voteService: NextCourseVoteService, val authenticationService: AuthenticationService) {

    @GetMapping
    fun getOptions(): ResponseEntity<List<NextCourseVoteOption>> {
        return if (authenticationService.isAuthenticated()) {
            ResponseEntity.ok(voteService.getOptions(authenticationService.getUserId()))
        } else {
            ResponseEntity.ok(voteService.getOptions())
        }
    }

    @PostMapping
    fun voteNextCourses(@RequestBody voteRequest: VoteNextCoursesRequest): ResponseEntity<Void> {
        if (authenticationService.isAuthenticated()) {
            voteService.addVote(authenticationService.getUserId(), voteRequest)
        } else {
            voteService.addVote(voteRequest = voteRequest)
        }
        return ResponseEntity.ok().build()
    }
}
