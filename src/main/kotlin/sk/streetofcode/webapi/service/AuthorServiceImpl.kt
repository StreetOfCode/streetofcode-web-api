package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.streetofcode.webapi.api.AuthorService
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.CourseReviewService
import sk.streetofcode.webapi.api.dto.AuthorOverviewDto
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.AuthorAddRequest
import sk.streetofcode.webapi.api.request.AuthorEditRequest
import sk.streetofcode.webapi.db.repository.AuthorRepository
import sk.streetofcode.webapi.db.repository.CourseRepository
import sk.streetofcode.webapi.model.Author
import sk.streetofcode.webapi.model.CourseStatus
import sk.streetofcode.webapi.model.toAuthorOverview
import sk.streetofcode.webapi.model.toCourseOverview

@Service
class AuthorServiceImpl(
    val authorRepository: AuthorRepository,
    val courseRepository: CourseRepository,
    val courseReviewService: CourseReviewService,
    val authenticationService: AuthenticationService,
    val courseProductService: CourseProductService
) : AuthorService {

    companion object {
        private val log = LoggerFactory.getLogger(AuthorServiceImpl::class.java)
    }

    override fun get(id: Long): Author {
        return authorRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Author with id $id was not found") }
    }
    override fun getOverview(slug: String): AuthorOverviewDto {
        val author = authorRepository.findBySlug(slug)
            .orElseThrow { ResourceNotFoundException("Author with slug $slug was not found") }

        val coursesToCourseReviewOverview = if (authenticationService.isAuthenticated() && authenticationService.isAdmin()) {
            author.courses.map {
                it.toCourseOverview(
                    courseReviewService.getCourseReviewsOverview(it.id!!),
                    null,
                    courseProductService.getAllForCourse(authenticationService.getNullableUserId(), it.id)
                )
            }
        } else {
            author.courses.filter { course -> course.status == CourseStatus.PUBLIC }
                .map {
                    it.toCourseOverview(
                        courseReviewService.getCourseReviewsOverview(it.id!!),
                        null,
                        courseProductService.getAllForCourse(authenticationService.getNullableUserId(), it.id)
                    )
                }
        }

        return author.toAuthorOverview(
            coursesToCourseReviewOverview
        )
    }

    override fun getAll(): List<Author> {
        return authorRepository.findAll().toList()
    }

    override fun add(addRequest: AuthorAddRequest): Author {
        val author = Author(addRequest.name, addRequest.slug, addRequest.imageUrl, addRequest.coursesTitle, addRequest.email, addRequest.description)

        try {
            return authorRepository.save(author)
        } catch (e: Exception) {
            log.error("Problem with saving author to db", e)
            throw InternalErrorException("Could not save author")
        }
    }

    override fun edit(id: Long, editRequest: AuthorEditRequest): Author {
        if (authorRepository.existsById(id)) {
            if (id != editRequest.id) {
                throw BadRequestException("PathVariable id is not equal to request id field")
            } else {
                val author = Author(editRequest.id, editRequest.name, editRequest.slug, editRequest.imageUrl, editRequest.coursesTitle, editRequest.email, editRequest.description)
                return authorRepository.save(author)
            }
        } else {
            throw ResourceNotFoundException("Author with id $id was not found")
        }
    }

    @Transactional
    override fun delete(id: Long): Author {
        val author = authorRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Author with id $id was not found") }

        // Change difficultyId to null in all courses with this difficultyId
        courseRepository.setAuthorsToNull(authorId = id)

        authorRepository.deleteById(id)

        return author
    }
}
