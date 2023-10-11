package sk.streetofcode.webapi.api.dto

import java.time.OffsetDateTime

data class UserProductDto(
    val priceId: String,
    val boughtAt: OffsetDateTime
)
