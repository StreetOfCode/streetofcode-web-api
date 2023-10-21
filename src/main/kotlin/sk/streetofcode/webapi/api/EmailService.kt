package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.SendFeedbackEmailRequest
import sk.streetofcode.webapi.model.CourseReview
import sk.streetofcode.webapi.model.CourseUserProduct
import sk.streetofcode.webapi.model.LectureComment
import sk.streetofcode.webapi.model.PostComment

interface EmailService {
    fun sendFeedbackEmail(userId: String? = null, request: SendFeedbackEmailRequest)
    fun sendDiscordInvitation(email: String)
    fun sendNewLectureCommentNotification(lectureComment: LectureComment)
    fun sendNewPostCommentNotification(postComment: PostComment)
    fun sendNewCourseReviewNotification(courseName: String, courseReview: CourseReview)
    fun sendCourseUserProductConfirmationMail(courseUserProduct: CourseUserProduct)
}
