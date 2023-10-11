package sk.streetofcode.webapi.api.request

// TODO STRIPE - move classes into files
data class CreatePaymentIntentRequest(
    val courseProductId: String
)

data class CreatePaymentIntentResponse(
    val clientSecret: String
)
