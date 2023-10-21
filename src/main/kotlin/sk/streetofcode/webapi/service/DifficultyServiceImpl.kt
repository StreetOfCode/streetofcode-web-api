package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.streetofcode.webapi.api.DifficultyService
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.DifficultyAddRequest
import sk.streetofcode.webapi.api.request.DifficultyEditRequest
import sk.streetofcode.webapi.db.repository.CourseRepository
import sk.streetofcode.webapi.db.repository.DifficultyRepository
import sk.streetofcode.webapi.model.Difficulty

@Service
class DifficultyServiceImpl(val difficultyRepository: DifficultyRepository, val courseRepository: CourseRepository) :
    DifficultyService {

    companion object {
        private val log = LoggerFactory.getLogger(DifficultyServiceImpl::class.java)
    }

    override fun get(id: Long): Difficulty {
        return difficultyRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Difficulty with id $id was not found") }
    }

    override fun getAll(): List<Difficulty> {
        return difficultyRepository.findAll().toList()
    }

    override fun add(addRequest: DifficultyAddRequest): Difficulty {
        val difficulty = Difficulty(addRequest.name, addRequest.skillLevel)

        try {
            return difficultyRepository.save(difficulty)
        } catch (e: Exception) {
            log.error("Problem with saving difficulty do db", e)
            throw InternalErrorException("Could not save difficulty")
        }
    }

    override fun edit(id: Long, editRequest: DifficultyEditRequest): Difficulty {
        if (difficultyRepository.existsById(id)) {
            if (id != editRequest.id) {
                throw BadRequestException("PathVariable id is not equal to request id field")
            } else {
                val difficulty = Difficulty(editRequest.id, editRequest.name, editRequest.skillLevel)
                return difficultyRepository.save(difficulty)
            }
        } else {
            throw ResourceNotFoundException("Difficulty with id $id was not found")
        }
    }

    @Transactional
    override fun delete(id: Long): Difficulty {
        val difficulty = difficultyRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Difficulty with id $id was not found") }

        // Change difficultyId to null in all courses with this difficultyId
        courseRepository.setDifficultyIdsToNull(difficultyId = id)

        difficultyRepository.deleteById(id)

        return difficulty
    }
}
