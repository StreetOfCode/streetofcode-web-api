package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.courseplatformbackend.api.EmailFeedbackService
import sk.streetofcode.courseplatformbackend.api.request.SendFeedbackEmailRequest

@RestController
@RequestMapping("email-feedback")
class EmailFeedbackController(private val emailFeedbackService: EmailFeedbackService) {
    @PostMapping
    fun sendFeedbackEmail(@RequestBody request: SendFeedbackEmailRequest): ResponseEntity<Void> {
        emailFeedbackService.sendFeedbackEmail(request)
        return ResponseEntity.ok().build()
    }
}
