package sk.streetofcode.courseplatformbackend.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.UserService
import sk.streetofcode.courseplatformbackend.api.exception.ConflictException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.UserAddRequest
import sk.streetofcode.courseplatformbackend.api.request.UserEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.UserRepository
import sk.streetofcode.courseplatformbackend.model.*

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
) : UserService {

    companion object {
        private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }

    override fun get(id: String): User {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id was not found") }
    }

    override fun add(id: String, userAddRequest: UserAddRequest): User {
        if (userRepository.existsById(id)) {
            throw ConflictException("User with id $id already added")
        }
        return userRepository.save(User(userAddRequest.id, userAddRequest.name, userAddRequest.email, userAddRequest.imageUrl))
    }

    override fun edit(id: String, userEditRequest: UserEditRequest): User {
        return userRepository.save(User(id, userEditRequest.name, userEditRequest.email, userEditRequest.imageUrl))
    }
}