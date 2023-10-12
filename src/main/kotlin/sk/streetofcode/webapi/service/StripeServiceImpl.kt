package sk.streetofcode.webapi.service

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.PaymentIntent
import com.stripe.model.StripeObject
import com.stripe.net.Webhook
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.EmailService
import sk.streetofcode.webapi.api.StripeService
import sk.streetofcode.webapi.api.UserProductService
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.request.CreatePaymentIntentRequest
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.client.stripe.StripeApiClient
import sk.streetofcode.webapi.client.stripe.configuration.StripeProperties
import sk.streetofcode.webapi.client.stripe.getMetadataFromPaymentIntent

@Service
class StripeServiceImpl(
    private val stripeProperties: StripeProperties,
    private val stripeApiClient: StripeApiClient,
    private val userProductService: UserProductService,
    private val emailService: EmailService,
) : StripeService {
    override fun createPaymentIntent(
        userId: String,
        createPaymentIntentRequest: CreatePaymentIntentRequest
    ): CreatePaymentIntentResponse {
        val product = stripeApiClient
            .getProduct(createPaymentIntentRequest.courseProductId)
        val price = product.price
        val amount = price.unitAmount

        return stripeApiClient.createPaymentIntent(
            userId,
            createPaymentIntentRequest.courseProductId,
            price.id,
            amount
        )
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
        val (userId, courseProductId, priceId) = getMetadataFromPaymentIntent(paymentIntent) ?: throw BadRequestException("Invalid metadata.")
        // callbacks after successful payment
        val userProduct = userProductService.addUserProduct(userId, courseProductId, priceId)
        emailService.sendUserProductConfirmationMail(userProduct)
    }
}
