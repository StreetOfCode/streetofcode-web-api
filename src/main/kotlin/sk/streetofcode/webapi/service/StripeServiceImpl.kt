package sk.streetofcode.webapi.service

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.PaymentIntent
import com.stripe.model.PromotionCode
import com.stripe.model.StripeObject
import com.stripe.net.Webhook
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.EmailService
import sk.streetofcode.webapi.api.StripeService
import sk.streetofcode.webapi.api.CourseUserProductService
import sk.streetofcode.webapi.api.dto.IsPromotionCodeValid
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.client.stripe.StripeApiClient
import sk.streetofcode.webapi.client.stripe.configuration.StripeProperties
import sk.streetofcode.webapi.client.stripe.getMetadataFromPaymentIntent

@Service
class StripeServiceImpl(
    private val stripeProperties: StripeProperties,
    private val stripeApiClient: StripeApiClient,
    private val userProductService: CourseUserProductService,
    private val emailService: EmailService,
    private val socUserServiceImpl: SocUserServiceImpl
) : StripeService {
    override fun createPaymentIntent(
        userId: String,
        courseProductId: String,
        promoCode: String?
    ): CreatePaymentIntentResponse {
        val product = stripeApiClient
            .getProduct(courseProductId)
        val price = product.price
        val fullAmount = price.unitAmount
        var discountAmount = 0L

        if (promoCode != null) {
            val promotionCode = getPromotionCode(promoCode)
            if (promotionCode.coupon.appliesTo.products.contains(product.product.id)) {
                discountAmount = promotionCode.coupon.amountOff
            }
        }

        val userEmail = socUserServiceImpl.get(userId).email

        return stripeApiClient.createPaymentIntent(
            userId,
            userEmail,
            courseProductId,
            fullAmount,
            discountAmount,
            promoCode
        )
    }

    override fun getPromotionCode(code: String): PromotionCode {
        return this.stripeApiClient.getPromotionCode(code)
    }

    override fun getIsPromotionCodeValid(code: String): IsPromotionCodeValid {
        return try {
            val promotionCode = this.stripeApiClient.getPromotionCode(code)
            if (promotionCode.active) {
                IsPromotionCodeValid(true, promotionCode.coupon.appliesTo.products)
            } else {
                IsPromotionCodeValid(false, null)
            }
        } catch (e: Exception) {
            IsPromotionCodeValid(false, null)
        }
    }

    override fun handleWebhook(body: String, signature: String) {
        val event = try {
            Webhook.constructEvent(body, signature, stripeProperties.webhookSecret)
        } catch (e: SignatureVerificationException) {
            throw BadRequestException("Invalid signature.")
        }

        // Deserialize the nested object inside the event
        val dataObjectDeserializer = event.dataObjectDeserializer
        var stripeObject: StripeObject = if (dataObjectDeserializer.getObject().isPresent) {
            dataObjectDeserializer.getObject().get()
        } else {
            throw BadRequestException("Event deserialization failed.")
        }

        when (event.type) {
            "payment_intent.succeeded" -> {
                val paymentIntent = stripeObject as PaymentIntent?
                handlePaymentSucceededEvent(paymentIntent!!)
            }
        }
    }

    private fun handlePaymentSucceededEvent(paymentIntent: PaymentIntent) {
        val (userId, courseProductId, appliedPromoCode) = getMetadataFromPaymentIntent(paymentIntent) ?: throw BadRequestException("Invalid metadata.")
        // callbacks after successful payment
        val courseUserProduct = userProductService.addCourseUserProduct(userId, courseProductId, appliedPromoCode)
        emailService.sendCourseUserProductConfirmationMail(courseUserProduct)
    }
}
