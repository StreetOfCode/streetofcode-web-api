package sk.streetofcode.webapi.client.stripe

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.model.Price
import com.stripe.model.Product
import com.stripe.model.PromotionCode
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.PriceListParams
import com.stripe.param.ProductListParams
import com.stripe.param.PromotionCodeListParams
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.client.stripe.configuration.StripeProperties
import java.lang.IllegalArgumentException
import javax.annotation.PostConstruct

@Service
class StripeApiClient(
    private val stripeProperties: StripeProperties
) {
    companion object {
        private val log = LoggerFactory.getLogger(StripeApiClient::class.java)
    }

    @PostConstruct
    fun init() {
        Stripe.apiKey = stripeProperties.apiKey
    }

    fun getProduct(productId: String): StripeProductWithPrice {
        val product = Product.list(ProductListParams.builder().addId(productId).build()).data.firstOrNull() ?: throw ResourceNotFoundException("Product with id $productId was not found")
        val price = Price.list(PriceListParams.builder().setProduct(productId).build()).data.firstOrNull() ?: throw ResourceNotFoundException("Price for product with id $productId was not found")

        return StripeProductWithPrice(product, price)
    }

    fun getPromotionCode(code: String): PromotionCode {
        // Returns only active promotionCode. PromotionCode contains info about discounted amount
        // and which products it applies to
        return PromotionCode.list(PromotionCodeListParams.builder().setCode(code).addExpand("data.coupon.applies_to").setActive(true).build()).data.firstOrNull()
            ?: throw ResourceNotFoundException("Promotion code with code $code was not found")
    }

    fun getProductPrice(productId: String): Long? {
        return try {
            val price = Price.list(PriceListParams.builder().setProduct(productId).build()).data.firstOrNull()
                ?: throw ResourceNotFoundException("Price for product with id $productId was not found")
            price.unitAmount
        } catch (e: Exception) {
            log.error("Error while getting price for product id: $productId", e)
            null
        }
    }

    fun createPaymentIntent(userId: String, userEmail: String, courseProductId: String, fullAmount: Long, discountAmount: Long, promoCode: String?): CreatePaymentIntentResponse {
        val finalAmount = fullAmount - discountAmount
        if (finalAmount <= 0) {
            log.error("Final amount for courseProductId $courseProductId is less or equal to zero")
            throw IllegalArgumentException("CreatePaymentIntent error - amount less or equal to zero")
        }

        val params = PaymentIntentCreateParams
            .builder()
            .setAmount(finalAmount)
            .setCurrency("eur")
            .setReceiptEmail(userEmail)
            .putAllMetadata(getPaymentIntentMetadataMap(userId, courseProductId, promoCode))
            .build()

        val paymentIntent = PaymentIntent.create(params)

        return CreatePaymentIntentResponse(paymentIntent.clientSecret, fullAmount, discountAmount, promoCode)
    }
}

class StripeProductWithPrice(
    val product: Product,
    val price: Price,
)
