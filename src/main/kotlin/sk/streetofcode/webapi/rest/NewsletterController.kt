package sk.streetofcode.webapi.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.EmailService
import sk.streetofcode.webapi.api.request.AddEmailToNewsletterRequest
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("newsletter")
class NewsletterController(
    private val emailService: EmailService,
    private val authenticationService: AuthenticationService
) {
    @PostMapping
    fun addToNewsletter(@RequestBody request: AddEmailToNewsletterRequest): ResponseEntity<Void> {
        if (authenticationService.isAuthenticated()) {
            emailService.addToNewsletter(authenticationService.getUserId(), request)
        } else {
            emailService.addToNewsletter(request = request)
        }
        return ResponseEntity.ok().build()
    }
}
