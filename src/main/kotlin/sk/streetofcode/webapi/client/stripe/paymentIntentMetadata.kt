package sk.streetofcode.webapi.client.stripe

import com.stripe.model.PaymentIntent

const val METADATA_KEY_USER_ID = "user_id"
const val METADATA_KEY_COURSE_PRODUCT_ID = "course_product_id"
const val METADATA_KEY_APPLIED_PROMO_CODE = "applied_promo_code"
const val METADATA_KEY_FINAL_AMOUNT = "final_amount"

fun getPaymentIntentMetadataMap(
    userId: String,
    courseProductId: String,
    finalAmount: Long,
    promoCode: String?
): Map<String, String?> {
    return mapOf(
        METADATA_KEY_USER_ID to userId,
        METADATA_KEY_COURSE_PRODUCT_ID to courseProductId,
        METADATA_KEY_FINAL_AMOUNT to finalAmount.toString(),
        METADATA_KEY_APPLIED_PROMO_CODE to promoCode
    )
}

fun getMetadataFromPaymentIntent(paymentIntent: PaymentIntent): PaymentMetadata? {
    if (!paymentIntent.metadata.containsKey(METADATA_KEY_USER_ID)
        || !paymentIntent.metadata.containsKey(METADATA_KEY_COURSE_PRODUCT_ID)
        || !paymentIntent.metadata.containsKey(METADATA_KEY_FINAL_AMOUNT)
    ) {
        return null
    }

    val userId = paymentIntent.metadata[METADATA_KEY_USER_ID]!!
    val courseProductId = paymentIntent.metadata[METADATA_KEY_COURSE_PRODUCT_ID]!!
    val finalAmount = paymentIntent.metadata[METADATA_KEY_FINAL_AMOUNT]!!.toLong()
    val appliedPromoCode = paymentIntent.metadata[METADATA_KEY_APPLIED_PROMO_CODE]

    return PaymentMetadata(userId, courseProductId, finalAmount, appliedPromoCode)
}

data class PaymentMetadata(
    val userId: String,
    val courseProductId: String,
    val finalAmount: Long,
    val appliedPromoCode: String?
)
