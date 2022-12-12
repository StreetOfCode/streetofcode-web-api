package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.SocUserService
import sk.streetofcode.webapi.api.exception.ConflictException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.SocUserAddRequest
import sk.streetofcode.webapi.api.request.SocUserEditRequest
import sk.streetofcode.webapi.client.convertkit.ConvertKitApiClient
import sk.streetofcode.webapi.db.repository.NewsletterRegistrationRepository
import sk.streetofcode.webapi.db.repository.SocUserRepository
import sk.streetofcode.webapi.model.NewsletterRegistration
import sk.streetofcode.webapi.model.SocUser
import sk.streetofcode.webapi.service.email.EmailServiceImpl

@Service
class SocUserServiceImpl(
    val socUserRepository: SocUserRepository,
    val convertKitApiClient: ConvertKitApiClient,
    val emailServiceImpl: EmailServiceImpl,
    val newsletterRegistrationRepository: NewsletterRegistrationRepository,
    val env: Environment
) : SocUserService {

    companion object {
        private val log = LoggerFactory.getLogger(SocUserServiceImpl::class.java)
    }

    override fun get(id: String): SocUser {
        return socUserRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id was not found") }
    }

    override fun add(id: String, socUserAddRequest: SocUserAddRequest): SocUser {
        if (socUserRepository.existsById(id)) {
            throw ConflictException("User with id $id already added")
        }

        if (socUserAddRequest.sendDiscordInvitation && env.activeProfiles.contains("prod")) {
            emailServiceImpl.sendDiscordInvitation(socUserAddRequest.email)
        }

        if (socUserAddRequest.receiveNewsletter) {
            convertKitApiClient.addSubscriber(socUserAddRequest.email, socUserAddRequest.name)
            newsletterRegistrationRepository.save(
                NewsletterRegistration(
                    firebaseId = id,
                    subscribedFrom = socUserAddRequest.subscribedFrom,
                )
            )
        }

        return socUserRepository.save(
            SocUser(
                socUserAddRequest.id,
                socUserAddRequest.name,
                socUserAddRequest.email,
                socUserAddRequest.imageUrl,
                socUserAddRequest.receiveNewsletter,
            )
        )
    }

    override fun edit(id: String, socUserEditRequest: SocUserEditRequest): SocUser {
        val user = socUserRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id was not found") }

        if (!user.receiveNewsletter && socUserEditRequest.receiveNewsletter) {
            newsletterRegistrationRepository.save(
                NewsletterRegistration(
                    firebaseId = id,
                    subscribedFrom = socUserEditRequest.subscribedFrom,
                )
            )
            convertKitApiClient.addSubscriber(user.email, socUserEditRequest.name)
        }

        return socUserRepository.save(
            SocUser(
                id,
                socUserEditRequest.name,
                user.email,
                socUserEditRequest.imageUrl,
                socUserEditRequest.receiveNewsletter,
            )
        )
    }
}
