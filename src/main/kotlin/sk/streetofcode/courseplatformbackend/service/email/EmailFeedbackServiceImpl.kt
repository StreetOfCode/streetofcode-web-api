package sk.streetofcode.courseplatformbackend.service.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.EmailFeedbackService
import sk.streetofcode.courseplatformbackend.api.request.SendFeedbackEmailRequest
import java.net.SocketTimeoutException

@Service
class EmailFeedbackServiceImpl(private val mailSender: JavaMailSender) : EmailFeedbackService {

    companion object {
        const val SUBJECT_PREFIX = "SoC feedback form - "
        const val SUBJECT_EMPTY = "no subject provided"
    }

    @Value("\${spring.mail.username}")
    private lateinit var emailFrom: String
    @Value("\${email-feedback.to-address}")
    private lateinit var emailTo: String

    override fun sendFeedbackEmail(request: SendFeedbackEmailRequest) {
        val message = SimpleMailMessage()
        message.setFrom(emailFrom)
        message.setTo(emailTo)

        message.setSubject(getSubject(request.subject))
        message.setReplyTo(request.email)
        message.setText(request.emailText)

        try {
            mailSender.send(message)
        } catch (e: MailException) {
            e.printStackTrace()
            // TODO log error
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            // TODO log error
        }
    }

    private fun getSubject(subject: String?): String {
        return if (subject != null && subject.isNotEmpty()) {
            SUBJECT_PREFIX + subject
        } else {
            SUBJECT_PREFIX + SUBJECT_EMPTY
        }
    }
}
