package sk.streetofcode.webapi.api.dto

import java.time.OffsetDateTime

data class CourseUserProductDto(
    val priceId: String,
    val boughtAt: OffsetDateTime
)
