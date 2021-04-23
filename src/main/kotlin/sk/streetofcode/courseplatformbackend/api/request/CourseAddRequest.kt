package sk.streetofcode.courseplatformbackend.api.request

import sk.streetofcode.courseplatformbackend.model.CourseStatus

data class CourseAddRequest(
    val authorId: Long,
    val difficultyId: Long,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val imageUrl: String? = null,
    val status: CourseStatus
)
