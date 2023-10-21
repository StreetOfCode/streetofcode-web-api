package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.CourseReviewService
import sk.streetofcode.webapi.api.CourseService
import sk.streetofcode.webapi.api.dto.CourseDto
import sk.streetofcode.webapi.api.dto.CourseOverviewDto
import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.api.exception.AuthorizationException
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.CourseAddRequest
import sk.streetofcode.webapi.api.request.CourseEditRequest
import sk.streetofcode.webapi.db.repository.AuthorRepository
import sk.streetofcode.webapi.db.repository.ChapterRepository
import sk.streetofcode.webapi.db.repository.CourseRepository
import sk.streetofcode.webapi.db.repository.DifficultyRepository
import sk.streetofcode.webapi.model.Course
import sk.streetofcode.webapi.model.CourseStatus
import sk.streetofcode.webapi.model.toCourseDto
import sk.streetofcode.webapi.model.toCourseOverview
import java.time.OffsetDateTime

@Service
class CourseServiceImpl(
    val courseRepository: CourseRepository,
    val chapterRepository: ChapterRepository,
    val authorRepository: AuthorRepository,
    val difficultyRepository: DifficultyRepository,
    val courseReviewService: CourseReviewService,
    // Use Lazy to prevent circular dependency error
    @Lazy
    val progressService: ProgressServiceImpl,
    val authenticationService: AuthenticationService,
    val courseProductService: CourseProductService
) : CourseService {

    companion object {
        private val log = LoggerFactory.getLogger(CourseServiceImpl::class.java)
    }

    override fun get(id: Long): CourseDto {
        return courseRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Course with id $id was not found") }
            .toCourseDto()
    }

    override fun getAll(): List<CourseDto> {
        return courseRepository.findAll().map { it.toCourseDto() }.toList()
    }

    override fun add(addRequest: CourseAddRequest): CourseDto {
        val author = authorRepository
            .findById(addRequest.authorId)
            .orElseThrow { ResourceNotFoundException("Author with id ${addRequest.authorId} was not found") }

        val difficulty = difficultyRepository
            .findById(addRequest.difficultyId)
            .orElseThrow { ResourceNotFoundException("Difficulty with id ${addRequest.difficultyId} was not found") }

        try {
            return courseRepository
                .save(
                    Course(
                        author,
                        difficulty,
                        addRequest.name,
                        addRequest.slug,
                        addRequest.shortDescription,
                        addRequest.longDescription,
                        addRequest.resources,
                        addRequest.trailerUrl,
                        addRequest.thumbnailUrl,
                        addRequest.iconUrl,
                        addRequest.status,
                        addRequest.courseOrder,
                    )
                )
                .toCourseDto()
        } catch (e: Exception) {
            log.error("Problem with saving course to db", e)
            throw InternalErrorException("Could not save course")
        }
    }

    override fun edit(id: Long, editRequest: CourseEditRequest): CourseDto {
        val existingCourse = courseRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Course with id $id was not found") }

        if (id != editRequest.id) {
            throw BadRequestException("PathVariable id is not equal to request id field")
        } else {
            val author = authorRepository
                .findById(editRequest.authorId)
                .orElseThrow { ResourceNotFoundException("Author with id ${editRequest.authorId} was not found") }

            val difficulty = difficultyRepository
                .findById(editRequest.difficultyId)
                .orElseThrow { ResourceNotFoundException("Difficulty with id ${editRequest.difficultyId} was not found") }

            existingCourse.author = author
            existingCourse.difficulty = difficulty
            existingCourse.name = editRequest.name
            existingCourse.slug = editRequest.slug
            existingCourse.shortDescription = editRequest.shortDescription
            existingCourse.longDescription = editRequest.longDescription
            existingCourse.resources = editRequest.resources
            existingCourse.trailerUrl = editRequest.trailerUrl
            existingCourse.thumbnailUrl = editRequest.thumbnailUrl
            existingCourse.iconUrl = editRequest.iconUrl
            existingCourse.status = editRequest.status
            existingCourse.updatedAt = OffsetDateTime.now()
            existingCourse.courseOrder = editRequest.courseOrder
            return courseRepository.save(existingCourse).toCourseDto()
        }
    }

    @Transactional
    override fun delete(id: Long): CourseDto {
        val course = get(id)

        chapterRepository.deleteByCourseId(id)
        courseRepository.deleteById(id)
        return course
    }

    override fun getPublicCoursesOverview(): List<CourseOverviewDto> {
        return getAllCoursesOverview().filter { CourseStatus.PUBLIC == it.status }
    }

    override fun getAllCoursesOverview(): List<CourseOverviewDto> {
        return courseRepository
            .findAll()
            .map {
                it.toCourseOverview(
                    courseReviewService.getCourseReviewsOverview(it.id!!),
                    getProgressMetadata(it.id),
                    courseProductService.getAllForCourse(it.id)
                )
            }
            .sortedBy { it.courseOrder }
            .toList()
    }

    private fun getProgressMetadata(courseId: Long): UserProgressMetadataDto? {
        return if (authenticationService.isAuthenticated()) {
            progressService.getUserProgressMetadataOrNull(authenticationService.getUserId(), courseId)
        } else {
            null
        }
    }

    override fun getPublicCourseOverview(userId: String?, slug: String): CourseOverviewDto {
        val course = courseRepository
            .findBySlug(slug)
            .orElseThrow { ResourceNotFoundException("Course with id $slug not found") }

        if (CourseStatus.PUBLIC != course.status) {
            throw AuthorizationException()
        } else {
            val progress =
                if (userId == null) null else progressService.getUserProgressMetadataOrNull(userId, course.id!!)
            val courseProducts = courseProductService.getAllForCourse(course.id!!)
            return course.toCourseOverview(
                courseReviewService.getCourseReviewsOverview(course.id),
                progress,
                courseProducts
            )
        }
    }

    override fun getAnyCourseOverview(userId: String?, slug: String): CourseOverviewDto {
        val course = courseRepository
            .findBySlug(slug)
            .orElseThrow { ResourceNotFoundException("Course with slug $slug not found") }

        val progress = if (userId == null) null else progressService.getUserProgressMetadataOrNull(userId, course.id!!)
        val courseProducts = courseProductService.getAllForCourse(course.id!!)
        return course.toCourseOverview(
            courseReviewService.getCourseReviewsOverview(course.id),
            progress,
            courseProducts
        )
    }

    override fun getMyCourses(userId: String): List<CourseOverviewDto> {
        val myCourseIds = progressService.getStartedCourseIds(userId)
        return myCourseIds
            .map { courseId ->
                courseRepository
                    .findById(courseId)
                    .get()
                    .toCourseOverview(
                        courseReviewService.getCourseReviewsOverview(courseId),
                        progressService.getUserProgressMetadata(userId, courseId),
                        courseProductService.getAllForCourse(courseId)
                    )
            }
    }
}
