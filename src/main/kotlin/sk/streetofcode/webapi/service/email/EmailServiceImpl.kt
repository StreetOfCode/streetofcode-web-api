package sk.streetofcode.webapi.service.email

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.EmailService
import sk.streetofcode.webapi.api.request.JavaCoursePromoCodeRequest
import sk.streetofcode.webapi.api.request.SendFeedbackEmailRequest
import sk.streetofcode.webapi.client.recaptcha.RecaptchaApiClient
import sk.streetofcode.webapi.model.CourseReview
import sk.streetofcode.webapi.model.CourseUserProduct
import sk.streetofcode.webapi.model.LectureComment
import sk.streetofcode.webapi.model.PostComment
import java.net.SocketTimeoutException

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val recaptchaApiClient: RecaptchaApiClient,
) : EmailService {

    companion object {
        private val log = LoggerFactory.getLogger(EmailServiceImpl::class.java)
        const val FEEDBACK_SUBJECT_PREFIX = "SoC feedback form - "
        const val SUBJECT_EMPTY = "no subject provided"

        const val DISCORD_INVITATION_HTML =
            "<p>Ahoj. Posielame ti pozvánku na náš Discord server.</p><a href=\"https://streetofcode.sk/discord\">Pridaj sa</a>"
    }

    @Value("\${emailservice.enabled:false}")
    private var enableEmailService: String = "false"

    @Value("\${spring.mail.username}")
    private lateinit var emailFrom: String

    @Value("\${email-feedback.to-address}")
    private lateinit var feedbackEmailTo: String

    @Value("\${java-course.basic.promo-code}")
    private lateinit var javaCourseBasicPromoCode: String

    @Value("\${java-course.premium.promo-code}")
    private lateinit var javaCoursePremiumPromoCode: String

    override fun sendDiscordInvitation(email: String) {
        if (enableEmailService != "true") return

        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "utf-8")

        message.setFrom(emailFrom, "Street of Code")
        message.setTo(email)

        message.setSubject("Discord pozvánka - Street of Code")
        message.setReplyTo(emailFrom)
        message.setText(DISCORD_INVITATION_HTML, true)

        try {
            mailSender.send(mimeMessage)
        } catch (e: MailException) {
            log.error("Problem with sending feedback email", e)
        } catch (e: SocketTimeoutException) {
            log.error("Timeout when reading from socket", e)
        }
    }

    override fun sendNewLectureCommentNotification(lectureComment: LectureComment) {
        if (enableEmailService != "true") return

        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "utf-8")

        message.setFrom(emailFrom, "Street of Code")
        message.setTo(emailFrom)

        message.setSubject("Nový komentár k lekcii - ${lectureComment.lecture.name}")
        message.setReplyTo(emailFrom)
        message.setText(createNewLectureCommentMessage(lectureComment), true)

        try {
            mailSender.send(mimeMessage)
        } catch (e: MailException) {
            log.error("Problem with sending lecture comment notification", e)
        } catch (e: SocketTimeoutException) {
            log.error("Timeout when reading from socket", e)
        }
    }

    override fun sendNewPostCommentNotification(postComment: PostComment) {
        if (enableEmailService != "true") return

        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "utf-8")

        message.setFrom(emailFrom, "Street of Code")
        message.setTo(emailFrom)

        message.setSubject("Nový komentár k postu - ${postComment.postSlug}")
        message.setReplyTo(emailFrom)
        message.setText(createNewPostCommentMessage(postComment), true)

        try {
            mailSender.send(mimeMessage)
        } catch (e: MailException) {
            log.error("Problem with sending post comment notification", e)
        } catch (e: SocketTimeoutException) {
            log.error("Timeout when reading from socket", e)
        }
    }

    override fun sendNewCourseReviewNotification(courseName: String, courseReview: CourseReview) {
        if (enableEmailService != "true") return

        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "utf-8")

        message.setFrom(emailFrom, "Street of Code")
        message.setTo(emailFrom)

        message.setSubject("Nový kurz review - $courseName")
        message.setReplyTo(emailFrom)
        message.setText(createNewReviewMessage(courseName, courseReview), true)

        try {
            mailSender.send(mimeMessage)
        } catch (e: MailException) {
            log.error("Problem with sending feedback email", e)
        } catch (e: SocketTimeoutException) {
            log.error("Timeout when reading from socket", e)
        }
    }

    override fun sendCourseUserProductConfirmationMail(courseUserProduct: CourseUserProduct) {
        if (enableEmailService != "true") return

        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "utf-8")

        message.setFrom(emailFrom, "Street of Code")
        message.setTo(courseUserProduct.socUser.email)

        message.setSubject("Úspešná objednávka - ${courseUserProduct.courseProduct.course.name}")
        message.setReplyTo(emailFrom)
        message.setText(createUserProductConfirmationMessage(courseUserProduct), true)

        try {
            mailSender.send(mimeMessage)
        } catch (e: MailException) {
            log.error("Problem with sending sendUserProductConfirmationMail email", e)
        } catch (e: SocketTimeoutException) {
            log.error("Timeout when reading from socket", e)
        }
    }

    override fun sendFeedbackEmail(userId: String?, request: SendFeedbackEmailRequest) {
        if (enableEmailService != "true") return

        if (userId == null) {
            if (request.recaptchaToken == null) {
                log.warn("Anonymous sends feedback request without recaptchaToken")
                return
            }

            if (!recaptchaApiClient.verifyRecaptchaToken(request.recaptchaToken)) {
                log.warn("Anonymous sends feedback request with failed verification of recaptcha token")
                return
            }
        }

        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "utf-8")

        message.setFrom(emailFrom, "Street of Code")
        message.setTo(request.email)

        message.setSubject("Java kurz - promo kód")
        message.setReplyTo(emailFrom)
    }

    override fun sendJavaCoursePromoCode(request: JavaCoursePromoCodeRequest) {
        if (enableEmailService != "true") return

        if (request.recaptchaToken != null) {
            if (!recaptchaApiClient.verifyRecaptchaToken(request.recaptchaToken)) {
                log.warn("Anonymous sends feedback request with failed verification of recaptcha token")
                return
            }
        }

        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "utf-8")

        message.setFrom(emailFrom, "Street of Code")
        message.setTo(request.email)

        message.setSubject("Java kurz - promo kód")
        message.setReplyTo(emailFrom)
        message.setText(createJavaCoursePromoCodeMessage(), true)

        try {
            mailSender.send(mimeMessage)
            log.info("Java promo codes sent to ${request.email}")
        } catch (e: MailException) {
            log.error("Problem with sending sendJavaCoursePromoCode email", e)
        } catch (e: SocketTimeoutException) {
            log.error("Timeout when reading from socket", e)
        }
    }

    private fun getSubject(subject: String?): String {
        return if (!subject.isNullOrEmpty()) {
            FEEDBACK_SUBJECT_PREFIX + subject
        } else {
            FEEDBACK_SUBJECT_PREFIX + SUBJECT_EMPTY
        }
    }

    private fun createNewLectureCommentMessage(comment: LectureComment): String {
        return "<h3>Používateľ</h3><p>Meno - ${comment.socUser.name}, Email - ${comment.socUser.email}, Id - ${comment.socUser.firebaseId}</p>" +
            "<h3>Kurz</h3><p>Názov - ${comment.lecture.chapter.course.name}" +
            "<h3>Lekcia</h3><p>Názov - ${comment.lecture.name}, Id - ${comment.lecture.id}</p>" +
            "<h3>Komentár</h3><p>${comment.commentText}</p>"
    }

    private fun createNewPostCommentMessage(comment: PostComment): String {
        return "<h3>Používateľ</h3><p>Meno - ${comment.socUser?.name}, Email - ${comment.socUser?.email}, Id - ${comment.socUser?.firebaseId}</p>" +
            "<h3>Post</h3><p>Názov - ${comment.postSlug}, Id - ${comment.postId}</p>" +
            "<h3>Komentár</h3><p>${comment.commentText}</p>"
    }

    private fun createNewReviewMessage(courseName: String, courseReview: CourseReview): String {
        return "<h3>Používateľ</h3><p>Meno - ${courseReview.socUser.name}, Email - ${courseReview.socUser.email}, Id - ${courseReview.socUser.firebaseId}</p>" +
            "<h3>Kurz</h3><p>Názov - $courseName, Id - ${courseReview.courseId}</p>" +
            "<h3>Review</h3><p>${courseReview.text}</p>" +
            "<h3>Rating</h3><p>${courseReview.rating}</p>"
    }

    private fun createUserProductConfirmationMessage(courseUserProduct: CourseUserProduct): String {
        return "<p>Ahoj.</p>" +
            "<p>Úspešne si si zakúpil/a ${courseUserProduct.courseProduct.course.name}.</p>" +
            " Prístup ku kurzu je napárovaný na tvoj email, a iba po prihlásení s týmto emailom na stránke si kurz budeš vedieť spustiť." +
            " Určite sa pridaj na náš Discord <a href=\"https://streetofcode.sk/discord\">server</a>, kde si budeme o kurze písať." +
            " Ak by si mal/a hocijaké otázky, alebo ti niečo nefunguje, tak neváhaj odpísať na túto správu.</p>" +
            "<p>Ďakujeme,</p>" +
            "<p>tím Street of Code.</p>"
    }

    private fun createJavaCoursePromoCodeMessage(): String {
        return "<p>Ahoj. Ďakujeme za záujem o náš Java kurz.</p>" +
            "<p>Promo kód pre základný balík je: <b>${this.javaCourseBasicPromoCode}</b></p>" +
            "<p>Promo kód pre premium balík je: <b>${this.javaCoursePremiumPromoCode}</b></p>" +
            "<p>Kódy sa dajú použiť do 12.11.2023 23:59.</p>" +
            "<p>Ďakujeme,</p>" +
            "<p>tím Street of Code.</p>" +
            "<p>P.S. Ak je to omyl, túto správu prosím ignoruj.</p>"
    }
}
