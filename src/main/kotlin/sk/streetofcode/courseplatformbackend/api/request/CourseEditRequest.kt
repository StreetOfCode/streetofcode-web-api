package sk.streetofcode.courseplatformbackend.api.request

data class CourseEditRequest(
        val id: Long,
        val authorId: Long,
        val difficultyId: Long,
        val name: String,
        val shortDescription: String,
        val longDescription: String
)