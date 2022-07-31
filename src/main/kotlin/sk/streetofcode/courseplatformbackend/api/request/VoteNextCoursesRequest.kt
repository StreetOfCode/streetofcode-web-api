package sk.streetofcode.courseplatformbackend.api.request

data class VoteNextCoursesRequest(
    val courseVoteOptionIds: List<Long>,
    val recaptchaToken: String?
)
