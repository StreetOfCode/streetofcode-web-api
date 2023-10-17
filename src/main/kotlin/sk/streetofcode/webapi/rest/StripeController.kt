package sk.streetofcode.webapi.rest

import com.stripe.model.PromotionCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.StripeService
import sk.streetofcode.webapi.api.dto.IsPromotionCodeValid
import sk.streetofcode.webapi.api.request.CreatePaymentIntentRequest
import sk.streetofcode.webapi.api.request.CreatePaymentIntentResponse
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("stripe")
class StripeController(val stripeService: StripeService, val authenticationService: AuthenticationService) {
    @PostMapping("create-payment-intent")
    @IsAuthenticated
    fun postPaymentIntent(@RequestBody createPaymentIntentRequest: CreatePaymentIntentRequest): ResponseEntity<CreatePaymentIntentResponse> =
        ResponseEntity.ok(stripeService.createPaymentIntent(authenticationService.getUserId(), createPaymentIntentRequest.courseProductId, createPaymentIntentRequest.promoCode))

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
