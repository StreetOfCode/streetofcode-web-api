package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
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

    override fun add(addRequest: AuthorAddRequest): Long {
        val author = Author(addRequest.name, addRequest.url, addRequest.description)
        return authorRepository.save(author).id ?: throw InternalErrorException("Could not save author")
    }

    // TODO this is not working
    override fun delete(id: Long) {
        if (authorRepository.existsById(id)) {
            // Remove all courses by this author
            // TODO We probably don't want to delete all courses when author is deleted.
            //  Then author_id in Course entity would have to be nullable
            courseRepository.deleteByAuthorId(id)

            authorRepository.deleteById(id)
        } else {
            throw ResourceNotFoundException("Author with id $id was not found")
        }
    }

}
