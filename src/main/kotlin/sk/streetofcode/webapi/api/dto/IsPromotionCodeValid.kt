package sk.streetofcode.webapi.api.dto

data class IsPromotionCodeValid(
    val isPromotionCodeValid: Boolean,
    val validForCourseProductId: List<String>?
)
