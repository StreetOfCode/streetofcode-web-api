package sk.streetofcode.webapi.api.request

data class DifficultyEditRequest(
    val id: Long,
    val name: String,
    val skillLevel: Int
)
