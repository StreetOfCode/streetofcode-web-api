package sk.streetofcode.webapi.api.request

data class VoteNextCoursesRequest(
    val courseVoteOptionIds: List<Long>,
    val recaptchaToken: String?
)
