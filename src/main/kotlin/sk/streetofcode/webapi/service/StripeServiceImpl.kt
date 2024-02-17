package sk.streetofcode.webapi.service

import com.stripe.exception.SignatureVerificationException
import com.stripe.exception.StripeException
import com.stripe.model.PaymentIntent
import com.stripe.model.PromotionCode
import com.stripe.model.StripeObject
import com.stripe.net.Webhook
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.CourseUserProductService
import sk.streetofcode.webapi.api.EmailService
import sk.streetofcode.webapi.api.StripeService
import sk.streetofcode.webapi.api.dto.IsPromotionCodeValid
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.api.request.UpdatePaymentIntentResponse
import sk.streetofcode.webapi.client.stripe.METADATA_KEY_COURSE_PRODUCT_ID
import sk.streetofcode.webapi.client.stripe.StripeApiClient
import sk.streetofcode.webapi.client.stripe.configuration.StripeProperties
import sk.streetofcode.webapi.client.stripe.getMetadataFromPaymentIntent

@Service
class StripeServiceImpl(
    private val stripeProperties: StripeProperties,
    private val stripeApiClient: StripeApiClient,
    private val userProductService: CourseUserProductService,
    private val emailService: EmailService,
    private val socUserServiceImpl: SocUserServiceImpl,
) : StripeService {

    companion object {
        private val log = LoggerFactory.getLogger(StripeServiceImpl::class.java)
    }

    override fun createPaymentIntent(
        userId: String,
        courseProductId: String,
    ): CreatePaymentIntentResponse {
        val amount = stripeApiClient.getProductPrice(courseProductId)
        val userEmail = socUserServiceImpl.get(userId).email

        return stripeApiClient.createPaymentIntent(
            userId,
            userEmail,
            courseProductId,
            amount
        )
    }

    override fun updatePaymentIntent(paymentIntentId: String, promoCode: String?): UpdatePaymentIntentResponse {
        val paymentIntent: PaymentIntent

        try {
            paymentIntent = PaymentIntent.retrieve(paymentIntentId)
        } catch (e: StripeException) {
            log.error("Error retrieving intent with id $paymentIntentId")
            throw ResourceNotFoundException("Could not find intent with id $paymentIntentId")
        }

        val courseProductId: String
        if (paymentIntent.metadata.containsKey(METADATA_KEY_COURSE_PRODUCT_ID)) {
            courseProductId = paymentIntent.metadata[METADATA_KEY_COURSE_PRODUCT_ID]!!
        } else {
            log.error("PaymentIntent doesn't have $METADATA_KEY_COURSE_PRODUCT_ID in metadata")
            throw InternalErrorException("Cannot update payment intent")
        }

        if (promoCode != null) {
            val fullAmount = paymentIntent.amount
            val discountAmount: Long
            val promotionCode = getPromotionCode(promoCode)
            if (promotionCode.coupon.appliesTo.products.contains(courseProductId)) {
                discountAmount = if (promotionCode.coupon.amountOff != null) {
                    promotionCode.coupon.amountOff
                } else if (promotionCode.coupon.percentOff != null) {
                    (fullAmount * promotionCode.coupon.percentOff.toLong() / 100)
                } else {
                    log.error("PromotionCode has neither amountOff nor percentOff")
                    throw InternalErrorException("Cannot update payment intent")
                }
            } else {
                log.error("PaymentIntent productId is not same as intended with PromotionCode. This should not happen, because only requests with same productId should be present")
                throw InternalErrorException("Cannot update payment intent")
            }

            if (fullAmount - discountAmount <= 0) {
                log.error("Final amount for courseProductId $courseProductId is less or equal to zero")
                throw InternalErrorException("CreatePaymentIntent error - amount less or equal to zero")
            }

            return stripeApiClient.updatePaymentIntent(paymentIntent, fullAmount, discountAmount, promoCode)
        } else {
            val productPrice = stripeApiClient.getProductPrice(courseProductId)
            return stripeApiClient.updatePaymentIntent(paymentIntent, productPrice, null, null)
        }
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
        val stripeObject: StripeObject = if (dataObjectDeserializer.getObject().isPresent) {
            dataObjectDeserializer.getObject().get()
        } else {
            throw BadRequestException("Event deserialization failed.")
        }

        when (event.type) {
            "payment_intent.succeeded" -> {
                val paymentIntent = stripeObject as PaymentIntent?
                handlePaymentSucceededEvent(paymentIntent!!)
            }
            else -> {
                log.info("Unhandled stripe event type: ${event.type}")
            }
        }
    }

    private fun handlePaymentSucceededEvent(paymentIntent: PaymentIntent) {
        val (userId, courseProductId, finalAmount, appliedPromoCode) = getMetadataFromPaymentIntent(paymentIntent)
            ?: throw BadRequestException("Invalid metadata.")

        if (userProductService.hasUserProduct(userId, courseProductId)) {
            log.info("User $userId already has product $courseProductId")
            return
        }

        log.info("Payment succeeded for user $userId and course product $courseProductId")

        // callbacks after successful payment
        val courseUserProduct =
            userProductService.addCourseUserProduct(userId, courseProductId, finalAmount, appliedPromoCode)
        emailService.sendCourseUserProductConfirmationMail(courseUserProduct)
    }
}
