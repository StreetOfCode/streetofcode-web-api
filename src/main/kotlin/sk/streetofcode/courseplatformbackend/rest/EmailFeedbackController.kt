package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.courseplatformbackend.api.EmailService
import sk.streetofcode.courseplatformbackend.api.request.SendFeedbackEmailRequest
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@RestController
@RequestMapping("email-feedback")
class EmailFeedbackController(
    private val emailService: EmailService,
    private val authenticationService: AuthenticationService
) {
    @PostMapping
    fun sendFeedbackEmail(@RequestBody request: SendFeedbackEmailRequest): ResponseEntity<Void> {
        if (authenticationService.isAuthenticated()) {
            emailService.sendFeedbackEmail(authenticationService.getUserId(), request)
        } else {
            emailService.sendFeedbackEmail(request = request)
        }
        return ResponseEntity.ok().build()
    }
}
