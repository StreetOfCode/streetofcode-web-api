package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.streetofcode.courseplatformbackend.api.DifficultyService
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.DifficultyRepository
import sk.streetofcode.courseplatformbackend.model.Difficulty
import java.lang.Exception

@Service
class DifficultyServiceImpl(val difficultyRepository: DifficultyRepository, val courseRepository: CourseRepository) : DifficultyService {
    override fun get(id: Long): Difficulty {
        return difficultyRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Difficulty with id $id was not found") }
    }

    override fun getAll(): List<Difficulty> {
        return difficultyRepository.findAll().toList()
    }

    override fun add(addRequest: DifficultyAddRequest): Difficulty {
        val difficulty = Difficulty(addRequest.name, addRequest.description)

        try {
            return difficultyRepository.save(difficulty)
        } catch (e: Exception) {
            throw InternalErrorException("Could not save difficulty")
        }
    }

    override fun edit(id: Long, editRequest: DifficultyEditRequest): Difficulty {
        if (difficultyRepository.existsById(id)) {
            if (id != editRequest.id) {
                throw BadRequestException("PathVariable id is not equal to request id field")
            } else {
                val difficulty = Difficulty(editRequest.id, editRequest.name, editRequest.description)
                return difficultyRepository.save(difficulty)
            }
        } else {
            throw ResourceNotFoundException("Difficulty with id $id was not found")
        }
    }

    @Transactional
    override fun delete(id: Long): Difficulty {
        val difficulty = difficultyRepository.findById(id)

        if (difficulty.isPresent) {
            // Change difficultyId to null in all courses with this difficultyId
            courseRepository.setDifficultyIdsToNull(difficultyId = id)

            difficultyRepository.deleteById(id)

            return difficulty.get()
        } else {
            throw ResourceNotFoundException("Difficulty with id $id was not found")
        }
    }
}
