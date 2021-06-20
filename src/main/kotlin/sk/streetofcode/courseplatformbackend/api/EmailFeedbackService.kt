package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.SendFeedbackEmailRequest

interface EmailFeedbackService {
    fun sendFeedbackEmail(request: SendFeedbackEmailRequest)
}
