package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseHomepageDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.mapper.CourseMapper
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.AuthorRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.DifficultyRepository
import sk.streetofcode.courseplatformbackend.model.Course
import java.time.OffsetDateTime

@Service
class CourseServiceImpl(val courseRepository: CourseRepository, val authorRepository: AuthorRepository, val difficultyRepository: DifficultyRepository, val chapterServiceImpl: ChapterServiceImpl, val mapper: CourseMapper) : CourseService {
    override fun get(id: Long): CourseDto {
        return mapper.toCourseDto(courseRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Course with id $id was not found") })
    }

    override fun getAll(): List<CourseDto> {
        return courseRepository.findAll().map { course -> mapper.toCourseDto(course) }.toList()
    }

    override fun add(addRequest: CourseAddRequest): CourseDto {
        val course = authorRepository.findById(addRequest.authorId)
        if (course.isEmpty) {
            throw ResourceNotFoundException("Author with id ${addRequest.authorId} was not found")
        }

        val difficulty = difficultyRepository.findById(addRequest.difficultyId)
        if (difficulty.isEmpty) {
            throw ResourceNotFoundException("Difficulty with id ${addRequest.difficultyId} was not found")
        }

        try {
            return mapper.toCourseDto(courseRepository.save(Course(course.get(), difficulty.get(), addRequest.name, addRequest.shortDescription, addRequest.longDescription, addRequest.imageUrl, addRequest.status)))
        } catch (e: Exception) {
            throw InternalErrorException("Could not save course")
        }
    }

    // TODO refactor
    override fun edit(id: Long, editRequest: CourseEditRequest): CourseDto {
        val existingCourse = courseRepository.findById(id)
        if (existingCourse.isEmpty) {
            throw ResourceNotFoundException("Course with id $id was not found")
        } else {
            if (id != editRequest.id) {
                throw BadRequestException("PathVariable id is not equal to request id field")
            } else {

                val author = authorRepository.findById(editRequest.authorId)
                if (author.isEmpty) {
                    throw ResourceNotFoundException("Author with id ${editRequest.authorId} was not found")
                }

                val difficulty = difficultyRepository.findById(editRequest.difficultyId)
                if (difficulty.isEmpty) {
                    throw ResourceNotFoundException("Difficulty with id ${editRequest.difficultyId} was not found")
                }

                val course = existingCourse.get()
                course.author = author.get()
                course.difficulty = difficulty.get()
                course.name = editRequest.name
                course.shortDescription = editRequest.shortDescription
                course.longDescription = editRequest.longDescription
                course.imageUrl = editRequest.imageUrl
                course.status = editRequest.status
                course.updatedAt = OffsetDateTime.now()
                return mapper.toCourseDto(courseRepository.save(course))
            }
        }
    }

    override fun delete(id: Long): CourseDto {
        val course = get(id)

        course.chapters.forEach { _ -> chapterServiceImpl.delete(course.id) }

        courseRepository.deleteById(id)
        return course
    }

    override fun getCoursesHomepage(): List<CourseHomepageDto> {
        return courseRepository.findAll().map { course -> mapper.toCourseHomepage(course) }.toList()
    }

    override fun getCourseOverview(id: Long): CourseOverviewDto {

        val course = courseRepository.findById(id)
        if (course.isPresent) {
            return mapper.toCourseOverview(course.get())
        } else {
            throw ResourceNotFoundException("Course with id $id not found")
        }
    }

}