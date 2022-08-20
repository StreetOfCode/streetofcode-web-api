package sk.streetofcode.webapi.api.request

import sk.streetofcode.webapi.model.CourseStatus

data class CourseAddRequest(
    val authorId: Long,
    val difficultyId: Long,
    val name: String,
    val slug: String,
    val shortDescription: String,
    val longDescription: String,
    val resources: String? = null,
    val trailerUrl: String? = null,
    val thumbnailUrl: String? = null,
    val iconUrl: String,
    val status: CourseStatus
)
