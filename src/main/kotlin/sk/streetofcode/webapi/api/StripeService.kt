package sk.streetofcode.webapi.api

import com.stripe.model.PromotionCode
import sk.streetofcode.webapi.api.dto.IsPromotionCodeValid
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.api.request.UpdatePaymentIntentResponse

interface StripeService {
    fun createPaymentIntent(userId: String, courseProductId: String): CreatePaymentIntentResponse
    fun updatePaymentIntent(paymentIntentId: String, promoCode: String?): UpdatePaymentIntentResponse
    fun getPromotionCode(code: String): PromotionCode
    fun getIsPromotionCodeValid(code: String): IsPromotionCodeValid
    fun handleWebhook(body: String, signature: String)
}
