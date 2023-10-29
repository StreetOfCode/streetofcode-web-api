package sk.streetofcode.webapi.service

import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.CourseReviewService
import sk.streetofcode.webapi.api.EmailService
import sk.streetofcode.webapi.api.SocUserService
import sk.streetofcode.webapi.api.dto.CourseReviewDto
import sk.streetofcode.webapi.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.webapi.api.exception.AuthorizationException
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.CourseReviewAddRequest
import sk.streetofcode.webapi.api.request.CourseReviewEditRequest
import sk.streetofcode.webapi.db.repository.CourseRepository
import sk.streetofcode.webapi.db.repository.CourseReviewRepository
import sk.streetofcode.webapi.model.CourseReview
import sk.streetofcode.webapi.model.toCourseReviewDto
import java.time.OffsetDateTime

@Service
class CourseReviewServiceImpl(
    private val courseReviewRepository: CourseReviewRepository,
    private val courseRepository: CourseRepository,
    private val authenticationService: AuthenticationService,
    private val socUserService: SocUserService,
    private val emailService: EmailService,
    private val courseProductService: CourseProductService
) : CourseReviewService {
    override fun getCourseReviews(courseId: Long): List<CourseReviewDto> {
        courseRepository
            .findById(courseId)
            .orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        return courseReviewRepository.findByCourseId(courseId).map { it.toCourseReviewDto() }
    }

    override fun getCourseReviewsOverview(courseId: Long): CourseReviewsOverviewDto {
        courseRepository.findById(courseId)
            .orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        val courseReviewsOverview = courseReviewRepository.getCourseReviewsOverview(courseId)
        return CourseReviewsOverviewDto(
            courseReviewsOverview.averageRating ?: 0.0,
            courseReviewsOverview.numberOfRatings ?: 0
        )
    }

    override fun get(id: Long): CourseReviewDto {
        return courseReviewRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Course review with id $id was not found") }
            .toCourseReviewDto()
    }

    override fun add(addRequest: CourseReviewAddRequest): CourseReviewDto {
        val course = courseRepository.findById(addRequest.courseId)
            .orElseThrow { ResourceNotFoundException("Course with id ${addRequest.courseId} was not found") }

        if (!courseProductService.isOwnedByUser(addRequest.courseId).isOwnedByUser
        ) {
            throw AuthorizationException("User does not own this course")
        }

        val userId = authenticationService.getUserId()
        val existingReview = courseReviewRepository.findBySocUserFirebaseIdAndCourseId(userId, addRequest.courseId)
        if (existingReview != null) {
            throw BadRequestException("User has already submitted a review for the given course")
        }

        validateRating(addRequest.rating)

        val courseReview = courseReviewRepository
            .save(CourseReview(socUserService.get(userId), addRequest.courseId, addRequest.rating, addRequest.text))

        emailService.sendNewCourseReviewNotification(course.name, courseReview)

        return courseReview.toCourseReviewDto()
    }

    override fun edit(id: Long, editRequest: CourseReviewEditRequest): CourseReviewDto {
        val review = courseReviewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Course review with id $id was not found") }

        validateUserAuthorization(review)
        validateRating(editRequest.rating)

        review.rating = editRequest.rating
        review.text = editRequest.text
        review.updatedAt = OffsetDateTime.now()

        return courseReviewRepository.save(review).toCourseReviewDto()
    }

    override fun delete(id: Long): CourseReviewDto {
        val review = courseReviewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Course review with id $id was not found") }

        validateUserAuthorization(review)

        courseReviewRepository.deleteById(id)
        return review.toCourseReviewDto()
    }

    private fun validateRating(rating: Double) {
        if (rating < 0 || rating > 5) {
            throw BadRequestException("Rating must be between 0 and 5")
        }
    }

    private fun validateUserAuthorization(review: CourseReview) {
        val userId = authenticationService.getUserId()
        if (review.socUser.firebaseId != userId && !authenticationService.isAdmin()) {
            throw AuthorizationException()
        }
    }
}
