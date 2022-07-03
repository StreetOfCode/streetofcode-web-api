package sk.streetofcode.courseplatformbackend.service

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.streetofcode.courseplatformbackend.api.CourseReviewService
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.courseplatformbackend.api.exception.AuthorizationException
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.AuthorRepository
import sk.streetofcode.courseplatformbackend.db.repository.ChapterRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.DifficultyRepository
import sk.streetofcode.courseplatformbackend.model.Course
import sk.streetofcode.courseplatformbackend.model.CourseStatus
import sk.streetofcode.courseplatformbackend.model.toCourseDto
import sk.streetofcode.courseplatformbackend.model.toCourseOverview
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
    val authenticationService: AuthenticationService
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
                        addRequest.shortDescription,
                        addRequest.longDescription,
                        addRequest.resources,
                        addRequest.trailerUrl,
                        addRequest.thumbnailUrl,
                        addRequest.iconUrl,
                        addRequest.status
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
            existingCourse.shortDescription = editRequest.shortDescription
            existingCourse.longDescription = editRequest.longDescription
            existingCourse.resources = editRequest.resources
            existingCourse.trailerUrl = editRequest.trailerUrl
            existingCourse.thumbnailUrl = editRequest.thumbnailUrl
            existingCourse.iconUrl = editRequest.iconUrl
            existingCourse.status = editRequest.status
            existingCourse.updatedAt = OffsetDateTime.now()
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
        return courseRepository
            .findAll()
            .filter { CourseStatus.PUBLIC == it.status }
            .map {
                it.toCourseOverview(
                    courseReviewService.getCourseReviewsOverview(it.id!!),
                    getProgressMetadata(it.id)
                )
            }
            .toList()
    }

    override fun getAllCoursesOverview(): List<CourseOverviewDto> {
        return courseRepository
            .findAll()
            .map {
                it.toCourseOverview(
                    courseReviewService.getCourseReviewsOverview(it.id!!),
                    getProgressMetadata(it.id)
                )
            }
            .toList()
    }

    private fun getProgressMetadata(courseId: Long): UserProgressMetadataDto? {
        return if (authenticationService.isAuthenticated()) {
            progressService.getUserProgressMetadataOrNull(authenticationService.getUserId(), courseId)
        } else {
            null
        }
    }

    override fun getPublicCourseOverview(userId: String?, id: Long): CourseOverviewDto {
        val course = courseRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Course with id $id not found") }

        if (CourseStatus.PUBLIC != course.status) {
            throw AuthorizationException()
        } else {
            val progress = if (userId == null) null else progressService.getUserProgressMetadataOrNull(userId, id)
            return course.toCourseOverview(
                courseReviewService.getCourseReviewsOverview(id),
                progress
            )
        }
    }

    override fun getAnyCourseOverview(userId: String?, id: Long): CourseOverviewDto {
        val course = courseRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Course with id $id not found") }

        val progress = if (userId == null) null else progressService.getUserProgressMetadataOrNull(userId, id)
        return course.toCourseOverview(courseReviewService.getCourseReviewsOverview(id), progress)
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
                        progressService.getUserProgressMetadata(userId, courseId)
                    )
            }
    }
}
