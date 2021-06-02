package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.CourseReviewService
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.courseplatformbackend.api.exception.AuthorizationException
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.mapper.CourseReviewMapper
import sk.streetofcode.courseplatformbackend.api.request.CourseReviewAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseReviewEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseReviewRepository
import sk.streetofcode.courseplatformbackend.model.CourseReview
import java.time.OffsetDateTime

@Service
class CourseReviewServiceImpl(
    private val mapper: CourseReviewMapper,
    private val courseReviewRepository: CourseReviewRepository,
    private val courseRepository: CourseRepository,
    private val authenticationService: AuthenticationService
) : CourseReviewService {
    override fun getCourseReviews(courseId: Long): List<CourseReviewDto> {
        courseRepository.findById(courseId).orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        return courseReviewRepository.findByCourseId(courseId).map { mapper.toCourseReviewDto(it) }
    }

    override fun getCourseReviewsOverview(courseId: Long): CourseReviewsOverviewDto {
        courseRepository.findById(courseId).orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        val courseReviewsOverview = courseReviewRepository.getCourseReviewsOverview(courseId)
        return CourseReviewsOverviewDto(courseReviewsOverview.averageRating ?: 0.0, courseReviewsOverview.numberOfRatings ?: 0)
    }

    override fun get(id: Long): CourseReviewDto {
        return mapper.toCourseReviewDto(courseReviewRepository.findById(id).orElseThrow { ResourceNotFoundException("Course review with id $id was not found") })
    }

    override fun add(addRequest: CourseReviewAddRequest): CourseReviewDto {
        courseRepository.findById(addRequest.courseId).orElseThrow { ResourceNotFoundException("Course with id ${addRequest.courseId} was not found") }

        val userId = authenticationService.getUserId()
        val existingReview = courseReviewRepository.findByUserIdAndCourseId(userId, addRequest.courseId)
        if (existingReview != null) {
            throw BadRequestException("User has already submitted a review for the given course")
        }

        validateRating(addRequest.rating)

        return mapper.toCourseReviewDto(
            courseReviewRepository.save(
                CourseReview(userId, addRequest.courseId, addRequest.rating, addRequest.text, addRequest.userName)
            )
        )
    }

    override fun edit(id: Long, editRequest: CourseReviewEditRequest): CourseReviewDto {
        val review = courseReviewRepository.findById(id).orElseThrow { ResourceNotFoundException("Course review with id $id was not found") }

        validateUserAuthorization(review)
        validateRating(editRequest.rating)

        review.rating = editRequest.rating
        review.text = editRequest.text
        review.userName = editRequest.userName
        review.updatedAt = OffsetDateTime.now()

        return mapper.toCourseReviewDto(courseReviewRepository.save(review))
    }

    override fun delete(id: Long): CourseReviewDto {
        val review = courseReviewRepository.findById(id).orElseThrow { ResourceNotFoundException("Course review with id $id was not found") }

        validateUserAuthorization(review)

        courseReviewRepository.deleteById(id)
        return mapper.toCourseReviewDto(review)
    }

    private fun validateRating(rating: Int) {
        if (rating < 0 || rating > 5) {
            throw BadRequestException("Rating must be between 0 and 5")
        }
    }

    private fun validateUserAuthorization(review: CourseReview) {
        val userId = authenticationService.getUserId()
        if (review.userId != userId && !authenticationService.isAdmin()) {
            throw AuthorizationException()
        }
    }
}
