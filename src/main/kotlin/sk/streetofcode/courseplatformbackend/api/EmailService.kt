package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.SendFeedbackEmailRequest

interface EmailService {
    fun sendFeedbackEmail(userId: String? = null, request: SendFeedbackEmailRequest)
    fun sendDiscordInvitation(email: String)
}
