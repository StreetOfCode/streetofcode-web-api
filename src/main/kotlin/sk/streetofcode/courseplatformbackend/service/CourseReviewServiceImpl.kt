package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.CourseReviewService
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.courseplatformbackend.api.exception.AuthorizationException
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
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
    override fun getCourseReviews(courseId: Long) = courseReviewRepository.findByCourseId(courseId).map { mapper.toCourseReviewDto(it) }

    override fun getCourseReviewsOverview(courseId: Long): CourseReviewsOverviewDto {
        courseRepository.findById(courseId).orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        return try {
            val courseReviewsOverview = courseReviewRepository.getCourseReviewsOverview(courseId)
            CourseReviewsOverviewDto(courseReviewsOverview.averageRating ?: 0.0, courseReviewsOverview.numberOfRatings ?: 0)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun add(addRequest: CourseReviewAddRequest): CourseReviewDto {
        courseRepository.findById(addRequest.courseId).orElseThrow { ResourceNotFoundException("Course with id ${addRequest.courseId} was not found") }

        validateRating(addRequest.rating)

        try {
            return mapper.toCourseReviewDto(
                courseReviewRepository.save(
                    CourseReview(authenticationService.getUserId(), addRequest.courseId, addRequest.rating, addRequest.text, addRequest.userName)
                )
            )
        } catch (e: Exception) {
            throw InternalErrorException("Could not save course review")
        }
    }

    override fun edit(id: Long, editRequest: CourseReviewEditRequest): CourseReviewDto {
        val review = courseReviewRepository.findById(id).orElseThrow { ResourceNotFoundException("Course review with id $id was not found") }

        validateUserAuthorization(review)
        validateRating(editRequest.rating)

        try {
            review.rating = editRequest.rating
            review.text = editRequest.text
            review.userName = editRequest.userName
            review.updatedAt = OffsetDateTime.now()

            return mapper.toCourseReviewDto(courseReviewRepository.save(review))
        } catch (e: Exception) {
            throw InternalErrorException("Could not edit course review")
        }
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
