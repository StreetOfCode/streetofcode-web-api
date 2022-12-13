package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.SendFeedbackEmailRequest
import sk.streetofcode.webapi.model.CourseReview
import sk.streetofcode.webapi.model.LectureComment

interface EmailService {
    fun sendFeedbackEmail(userId: String? = null, request: SendFeedbackEmailRequest)
    fun sendDiscordInvitation(email: String)
    fun sendNewLectureCommentNotification(lectureComment: LectureComment)
    fun sendNewCourseReviewNotification(courseName: String, courseReview: CourseReview)
}
