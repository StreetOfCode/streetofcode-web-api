package sk.streetofcode.courseplatformbackend.service

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.SocUserService
import sk.streetofcode.courseplatformbackend.api.exception.ConflictException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.SocUserAddRequest
import sk.streetofcode.courseplatformbackend.api.request.SocUserEditRequest
import sk.streetofcode.courseplatformbackend.client.convertkit.ConvertKitApiClient
import sk.streetofcode.courseplatformbackend.db.repository.SocUserRepository
import sk.streetofcode.courseplatformbackend.model.SocUser
import sk.streetofcode.courseplatformbackend.service.email.EmailServiceImpl

@Service
class SocUserServiceImpl(
    val socUserRepository: SocUserRepository,
    val convertKitApiClient: ConvertKitApiClient,
    val emailServiceImpl: EmailServiceImpl,
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

        if (socUserAddRequest.receiveNewsletter && env.activeProfiles.contains("prod")) {
            convertKitApiClient.addSubscriber(socUserAddRequest.email, socUserAddRequest.name)
        }

        return socUserRepository.save(SocUser(socUserAddRequest.id, socUserAddRequest.name, socUserAddRequest.email, socUserAddRequest.imageUrl, socUserAddRequest.receiveNewsletter))
    }

    override fun edit(id: String, socUserEditRequest: SocUserEditRequest): SocUser {
        val user = socUserRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id was not found") }

        if (!user.receiveNewsletter && socUserEditRequest.receiveNewsletter && env.activeProfiles.contains("prod")) {
            convertKitApiClient.addSubscriber(user.email, socUserEditRequest.name)
        }

        return socUserRepository.save(SocUser(id, socUserEditRequest.name, user.email, socUserEditRequest.imageUrl, socUserEditRequest.receiveNewsletter))
    }
}
