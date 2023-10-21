package sk.streetofcode.webapi.api.dto

import java.time.OffsetDateTime

data class CourseUserProductDto(
    val boughtAt: OffsetDateTime,
    val finalAmount: Long,
    val usedPromoCode: String? = null
)
