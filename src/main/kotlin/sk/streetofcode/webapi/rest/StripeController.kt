package sk.streetofcode.webapi.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.webapi.api.StripeService
import sk.streetofcode.webapi.api.dto.IsPromotionCodeValid
import sk.streetofcode.webapi.api.request.CreatePaymentIntentRequest
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.api.request.UpdatePaymentIntentRequest
import sk.streetofcode.webapi.api.request.UpdatePaymentIntentResponse
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("stripe")
class StripeController(val stripeService: StripeService, val authenticationService: AuthenticationService) {
    @PostMapping("create-payment-intent")
    @IsAuthenticated
    fun createPaymentIntent(@RequestBody createPaymentIntentRequest: CreatePaymentIntentRequest): ResponseEntity<CreatePaymentIntentResponse> =
        ResponseEntity.ok(stripeService.createPaymentIntent(authenticationService.getUserId(), createPaymentIntentRequest.courseProductId))

    @PostMapping("update-payment-intent")
    @IsAuthenticated
    fun updatePaymentIntent(@RequestBody updatePaymentIntentRequest: UpdatePaymentIntentRequest): ResponseEntity<UpdatePaymentIntentResponse> =
        ResponseEntity.ok(stripeService.updatePaymentIntent(updatePaymentIntentRequest.paymentIntentId, updatePaymentIntentRequest.promoCode))

    @PostMapping("webhook")
    fun postWebhook(@RequestHeader("Stripe-Signature") stripeSignature: String, @RequestBody body: String): ResponseEntity<String> {
        stripeService.handleWebhook(body, stripeSignature)
        return ResponseEntity.ok().build()
    }

    @GetMapping("check-promotion-code/{code}")
    @IsAuthenticated
    fun getIsPromotionCodeValid(@PathVariable("code") code: String): ResponseEntity<IsPromotionCodeValid> {
        return ResponseEntity.ok(stripeService.getIsPromotionCodeValid(code))
    }
}
