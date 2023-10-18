package sk.streetofcode.webapi.api.request

data class CreatePaymentIntentRequest(
    val courseProductId: String,
)

data class UpdatePaymentIntentRequest(
    val paymentIntentId: String,
    val promoCode: String
)

data class CreatePaymentIntentResponse(
    val clientSecret: String,
    val paymentIntentId: String,
    val fullAmount: Long,
    val discountAmount: Long?,
    val promoCode: String?
)

data class UpdatePaymentIntentResponse(
    val clientSecret: String,
    val paymentIntentId: String,
    val fullAmount: Long,
    val discountAmount: Long,
    val promoCode: String
)
