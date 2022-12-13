package sk.streetofcode.webapi.service.email

import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.NewsletterService
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.AddEmailToNewsletterRequest
import sk.streetofcode.webapi.client.convertkit.ConvertKitApiClient
import sk.streetofcode.webapi.client.recaptcha.RecaptchaApiClient
import sk.streetofcode.webapi.db.repository.NewsletterRegistrationRepository
import sk.streetofcode.webapi.db.repository.SocUserRepository
import sk.streetofcode.webapi.model.NewsletterRegistration
import sk.streetofcode.webapi.model.SocUser

@Service
class NewsletterServiceImpl(
    private val recaptchaApiClient: RecaptchaApiClient,
    private val convertKitApiClient: ConvertKitApiClient,
    private val socUserRepository: SocUserRepository,
    private val newsletterRegistrationRepository: NewsletterRegistrationRepository,
) : NewsletterService {

    companion object {
        private val log = LoggerFactory.getLogger(NewsletterServiceImpl::class.java)
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

            newsletterRegistrationRepository.save(
                NewsletterRegistration(
                    firebaseId = null,
                    subscribedFrom = request.subscribedFrom,
                    fromPath = request.fromPath
                )
            )
            convertKitApiClient.addSubscriber(request.email, null)
        } else {
            val user = socUserRepository.findById(userId)
                .orElseThrow { ResourceNotFoundException("User with id $id was not found") }

            if (user.receiveNewsletter) {
                log.warn("User sends add newsletter request but already receives newsletter")
                return
            }

            newsletterRegistrationRepository.save(
                NewsletterRegistration(
                    firebaseId = user.firebaseId,
                    subscribedFrom = request.subscribedFrom,
                    fromPath = request.fromPath
                )
            )
            socUserRepository.save(SocUser(userId, user.name, user.email, user.imageUrl, true))
            convertKitApiClient.addSubscriber(request.email, user.email)
        }
    }
}
