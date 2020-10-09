package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.DifficultyService
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.DifficultyRepository
import sk.streetofcode.courseplatformbackend.model.Difficulty

@Service
class DifficultyServiceImpl(val difficultyRepository: DifficultyRepository, val courseRepository: CourseRepository) : DifficultyService {
    override fun get(id: Long): Difficulty {
        return difficultyRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Difficulty with id $id was not found") }
    }

    override fun getAll(): List<Difficulty> {
        return difficultyRepository.findAll().toList()
    }

    override fun add(addRequest: DifficultyAddRequest): Long {
        val difficulty = Difficulty(addRequest.name, addRequest.description, addRequest.difficultyOrder)

        return difficultyRepository.save(difficulty).id ?: throw InternalErrorException("Could not save difficulty")
    }

    override fun delete(id: Long) {
        get(id) // If this line line wont throw exception then it means that difficulty by this id exists

        // Remove all courses with difficultyId when removed
        // TODO We probably don't want to delete all courses when difficulty is deleted.
        //  Then difficulty_id in Course entity would have to be nullable
        courseRepository.deleteByDifficultyId(id)

        difficultyRepository.deleteById(id)
    }

}