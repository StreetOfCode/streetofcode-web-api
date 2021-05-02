package sk.streetofcode.courseplatformbackend.api.request

data class LectureCommentAddRequest(
    val userName: String,
    val commentText: String
)
