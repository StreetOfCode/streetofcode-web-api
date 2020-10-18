package sk.streetofcode.courseplatformbackend.api.request

data class DifficultyEditRequest(
        val id: Long,
        val name: String,
        val description: String,
        val difficultyOrder: Int
)