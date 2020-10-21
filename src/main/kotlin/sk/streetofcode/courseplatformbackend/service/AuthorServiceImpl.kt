package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.api.request.AuthorEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.AuthorRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.model.Author

@Service
class AuthorServiceImpl(val authorRepository: AuthorRepository, val courseRepository: CourseRepository) : AuthorService {
    override fun get(id: Long): Author {
        return authorRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Author with id $id was not found") }
    }

    override fun getAll(): List<Author> {
        return authorRepository.findAll().toList()
    }

    override fun add(addRequest: AuthorAddRequest): Author {
        val author = Author(addRequest.name, addRequest.url, addRequest.description)

        try {
            return authorRepository.save(author)
        } catch (e: Exception) {
            throw InternalErrorException("Could not save author")
        }
    }

    override fun edit(id: Long, editRequest: AuthorEditRequest): Author {
        if (authorRepository.existsById(id)) {
            if (id != editRequest.id) {
                throw BadRequestException("PathVariable id is not equal to request id field")
            } else {
                val author = Author(editRequest.id, editRequest.name, editRequest.url, editRequest.description)
                return authorRepository.save(author)
            }
        } else {
            throw ResourceNotFoundException("Author with id $id was not found")
        }
    }


    @Transactional
    override fun delete(id: Long) : Author {
        val author = authorRepository.findById(id)

        if (author.isPresent) {
            // Change difficultyId to null in all courses with this difficultyId
            courseRepository.setAuthorsToNull(authorId = id)

            authorRepository.deleteById(id)

            return author.get()

        } else {
            throw ResourceNotFoundException("Author with id $id was not found")
        }
    }

}
