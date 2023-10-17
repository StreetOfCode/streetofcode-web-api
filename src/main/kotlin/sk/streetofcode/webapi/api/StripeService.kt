package sk.streetofcode.webapi.api

import com.stripe.model.PromotionCode
import sk.streetofcode.webapi.api.dto.IsPromotionCodeValid
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse

interface StripeService {
    fun createPaymentIntent(userId: String, courseProductId: String, promoCode: String?): CreatePaymentIntentResponse
    fun getPromotionCode(code: String): PromotionCode
    fun getIsPromotionCodeValid(code: String): IsPromotionCodeValid
    fun handleWebhook(body: String, signature: String)
}
