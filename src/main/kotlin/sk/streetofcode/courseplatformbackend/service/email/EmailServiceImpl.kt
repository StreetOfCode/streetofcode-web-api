package sk.streetofcode.courseplatformbackend.service.email

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.EmailService
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.AddEmailToNewsletterRequest
import sk.streetofcode.courseplatformbackend.api.request.SendFeedbackEmailRequest
import sk.streetofcode.courseplatformbackend.client.convertkit.ConvertKitApiClient
import sk.streetofcode.courseplatformbackend.client.recaptcha.RecaptchaApiClient
import sk.streetofcode.courseplatformbackend.db.repository.SocUserRepository
import sk.streetofcode.courseplatformbackend.model.SocUser
import java.net.SocketTimeoutException

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val recaptchaApiClient: RecaptchaApiClient,
    private val convertKitApiClient: ConvertKitApiClient,
    private val socUserRepository: SocUserRepository
) : EmailService {

    companion object {
        private val log = LoggerFactory.getLogger(EmailServiceImpl::class.java)
        const val FEEDBACK_SUBJECT_PREFIX = "SoC feedback form - "
        const val SUBJECT_EMPTY = "no subject provided"

        const val DISCORD_INVITATION_HTML = "<p>Ahoj. Posielame ti pozvánku na náš Discord server.</p><a href=\"https://discord.com/invite/7K4dG6Nru4\">Pridaj sa</a>"
    }

    @Value("\${spring.mail.username}")
    private lateinit var emailFrom: String

    @Value("\${email-feedback.to-address}")
    private lateinit var feedbackEmailTo: String

    override fun sendDiscordInvitation(email: String) {
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

    override fun addToNewsletter(userId: String?, request: AddEmailToNewsletterRequest) {
        if (userId == null) {
            if (request.recaptchaToken == null) {
                log.warn("Anonymous sends add newsletter request without recaptchaToken")
                return
            }

            if (!recaptchaApiClient.verifyRecaptchaToken(request.recaptchaToken)) {
                log.warn("Anonymous sends add newsletter request with failed verification of recaptcha token")
                return
            }

            convertKitApiClient.addSubscriber(request.email, null)
        } else {
            val user = socUserRepository.findById(userId)
                .orElseThrow { ResourceNotFoundException("User with id $id was not found") }

            if (user.receiveNewsletter) {
                log.warn("User sends add newsletter request but already receives newsletter")
                return
            }

            socUserRepository.save(SocUser(userId, user.name, user.email, user.imageUrl, true))
            convertKitApiClient.addSubscriber(request.email, user.email)
        }
    }

    override fun sendFeedbackEmail(userId: String?, request: SendFeedbackEmailRequest) {
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

        val message = SimpleMailMessage()
        message.setFrom(emailFrom)
        message.setTo(feedbackEmailTo)

        message.setSubject(getSubject(request.subject))
        message.setReplyTo(request.email)
        message.setText(request.emailText)

        try {
            mailSender.send(message)
        } catch (e: MailException) {
            log.error("Problem with sending feedback email", e)
        } catch (e: SocketTimeoutException) {
            log.error("Timeout when reading from socket", e)
        }
    }

    private fun getSubject(subject: String?): String {
        return if (subject != null && subject.isNotEmpty()) {
            FEEDBACK_SUBJECT_PREFIX + subject
        } else {
            FEEDBACK_SUBJECT_PREFIX + SUBJECT_EMPTY
        }
    }
}