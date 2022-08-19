package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.courseplatformbackend.api.EmailService
import sk.streetofcode.courseplatformbackend.api.request.AddEmailToNewsletterRequest
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

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
