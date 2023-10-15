package sk.streetofcode.webapi.api.dto

data class CourseProductDto(
    val productId: String,
    val courseId: Long,
    val courseUserProducts: List<CourseUserProductDto>,
    val price: Long?
)

data class IsOwnedByUserDto(
    val isOwnedByUser: Boolean
)
