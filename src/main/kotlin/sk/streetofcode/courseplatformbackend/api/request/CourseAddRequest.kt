package sk.streetofcode.courseplatformbackend.api.request

data class CourseAddRequest(
        val authorId: Long,
        val difficultyId: Long,
        val name: String,
        val shortDescription: String,
        val longDescription: String,
        val imageUrl: String? = null
)