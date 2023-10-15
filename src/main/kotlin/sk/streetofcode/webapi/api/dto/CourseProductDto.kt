package sk.streetofcode.webapi.api.dto

import sk.streetofcode.webapi.client.stripe.StripeProductWithPrice

data class CourseProductDto(
    val productId: String,
    val courseId: Long,
    val userProducts: List<UserProductDto>,
    val price: Long?
)

data class IsOwnedByUserDto(
    val isOwnedByUser: Boolean
)
