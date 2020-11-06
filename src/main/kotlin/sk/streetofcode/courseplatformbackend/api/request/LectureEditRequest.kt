package sk.streetofcode.courseplatformbackend.api.request

data class LectureEditRequest(
        val id: Long,
        val name: String,
        val lectureOrder: Int,
        val content: String
)