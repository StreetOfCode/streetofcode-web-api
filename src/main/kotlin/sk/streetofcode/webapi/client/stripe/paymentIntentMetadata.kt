package sk.streetofcode.webapi.client.stripe

import com.stripe.model.PaymentIntent

const val METADATA_KEY_USER_ID = "user_id"
const val METADATA_KEY_COURSE_PRODUCT_ID = "course_product_id"
const val METADATA_KEY_APPLIED_PROMO_CODE = "applied_promo_code"

fun getPaymentIntentMetadataMap(userId: String, courseProductId: String, promoCode: String?): Map<String, String?> {
    return mapOf(
        METADATA_KEY_USER_ID to userId,
        METADATA_KEY_COURSE_PRODUCT_ID to courseProductId,
        METADATA_KEY_APPLIED_PROMO_CODE to promoCode
    )
}

fun getMetadataFromPaymentIntent(paymentIntent: PaymentIntent): Triple<String, String, String?>? {
    if (!paymentIntent.metadata.containsKey(METADATA_KEY_USER_ID)
        || !paymentIntent.metadata.containsKey(METADATA_KEY_COURSE_PRODUCT_ID)) {
        return null
    }

    val userId = paymentIntent.metadata[METADATA_KEY_USER_ID]!!
    val courseProductId = paymentIntent.metadata[METADATA_KEY_COURSE_PRODUCT_ID]!!
    val appliedPromoCode = paymentIntent.metadata[METADATA_KEY_APPLIED_PROMO_CODE]

    // TODO appliedPromoCode not working, it reads null from map even when it was inserted in paymentIntent before

    return Triple(userId, courseProductId, appliedPromoCode)
}
