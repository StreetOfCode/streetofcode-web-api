package sk.streetofcode.webapi.client.stripe

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.model.Price
import com.stripe.model.PromotionCode
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.PaymentIntentUpdateParams
import com.stripe.param.PriceListParams
import com.stripe.param.PromotionCodeListParams
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.api.request.UpdatePaymentIntentResponse
import sk.streetofcode.webapi.client.stripe.configuration.StripeProperties
import javax.annotation.PostConstruct

@Service
class StripeApiClient(
    private val stripeProperties: StripeProperties
) {
    @PostConstruct
    fun init() {
        Stripe.apiKey = stripeProperties.apiKey
    }

    fun getPromotionCode(code: String): PromotionCode {
        // Returns only active promotionCode. PromotionCode contains info about discounted amount
        // and which products it applies to
        return PromotionCode.list(
            PromotionCodeListParams.builder().setCode(code).addExpand("data.coupon.applies_to").setActive(true).build()
        ).data.firstOrNull()
            ?: throw ResourceNotFoundException("Promotion code with code $code was not found")
    }

    fun getProductPrice(productId: String): Long {
        val price = Price.list(PriceListParams.builder().setProduct(productId).build()).data.firstOrNull()
            ?: throw InternalErrorException("Product price is null")
        return price.unitAmount
    }

    fun createPaymentIntent(
        userId: String,
        userEmail: String,
        courseProductId: String,
        amount: Long
    ): CreatePaymentIntentResponse {
        val params = PaymentIntentCreateParams
            .builder()
            .setAmount(amount)
            .setCurrency("eur")
            .setReceiptEmail(userEmail)
            .putAllMetadata(getPaymentIntentMetadataMap(userId, courseProductId, amount, null))
            .build()

        val paymentIntent = PaymentIntent.create(params)

        return CreatePaymentIntentResponse(
            paymentIntent.clientSecret,
            paymentIntent.id,
            amount,
            null,
            null,
        )
    }

    fun updatePaymentIntent(
        paymentIntent: PaymentIntent,
        fullAmount: Long,
        discountAmount: Long?,
        promoCode: String?
    ): UpdatePaymentIntentResponse {
        val (userId, courseProductId, _) = getMetadataFromPaymentIntent(paymentIntent)
            ?: throw BadRequestException("Invalid metadata.")

        val finalAmount = fullAmount - (discountAmount ?: 0)

        paymentIntent.update(
            PaymentIntentUpdateParams
                .builder()
                .setAmount(finalAmount)
                .putAllMetadata(getPaymentIntentMetadataMap(userId, courseProductId, finalAmount, promoCode))
                .build()
        )

        return UpdatePaymentIntentResponse(
            clientSecret = paymentIntent.clientSecret,
            paymentIntent.id,
            fullAmount,
            discountAmount,
            promoCode,
        )
    }
}
