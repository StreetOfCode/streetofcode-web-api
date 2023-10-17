package sk.streetofcode.webapi.api.request

data class CreatePaymentIntentRequest(
    val courseProductId: String,
    val promoCode: String?
)

data class CreatePaymentIntentResponse(
    val clientSecret: String,
    val fullPriceAmount: Long,
    val discountAmount: Long,
    val promoCode: String?
)
