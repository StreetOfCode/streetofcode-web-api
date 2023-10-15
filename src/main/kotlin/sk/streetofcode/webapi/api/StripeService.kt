package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.CreatePaymentIntentRequest
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse

interface StripeService {
    fun createPaymentIntent(userId: String, courseProductId: String): CreatePaymentIntentResponse
    fun handleWebhook(body: String, signature: String)
}
