package sk.streetofcode.webapi.api.request

data class CreatePaymentIntentRequest(
    val courseProductId: String
)

data class CreatePaymentIntentResponse(
    val clientSecret: String
)
