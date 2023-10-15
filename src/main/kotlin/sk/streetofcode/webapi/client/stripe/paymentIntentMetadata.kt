package sk.streetofcode.webapi.client.stripe

import com.stripe.model.PaymentIntent

const val METADATA_KEY_USER_ID = "user_id"
const val METADATA_KEY_COURSE_PRODUCT_ID = "course_product_id"

fun getPaymentIntentMetadataMap(userId: String, courseProductId: String): Map<String, String> {
    return mapOf(
        METADATA_KEY_USER_ID to userId,
        METADATA_KEY_COURSE_PRODUCT_ID to courseProductId,
    )
}

fun getMetadataFromPaymentIntent(paymentIntent: PaymentIntent): Pair<String, String>? {
    if (!paymentIntent.metadata.containsKey(METADATA_KEY_USER_ID)
        || !paymentIntent.metadata.containsKey(METADATA_KEY_COURSE_PRODUCT_ID)) {
        return null
    }

    val userId = paymentIntent.metadata[METADATA_KEY_USER_ID]!!
    val courseProductId = paymentIntent.metadata[METADATA_KEY_COURSE_PRODUCT_ID]!!

    return Pair(userId, courseProductId)
}
