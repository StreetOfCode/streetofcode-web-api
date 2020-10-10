package sk.streetofcode.courseplatformbackend.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.db.projection.CourseOverview
import sk.streetofcode.courseplatformbackend.db.projection.CoursesHomepageOverview
import sk.streetofcode.courseplatformbackend.db.repository.AuthorRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.DifficultyRepository
import sk.streetofcode.courseplatformbackend.model.Course

@Service
class CourseServiceImpl(val courseRepository: CourseRepository, val authorRepository: AuthorRepository, val difficultyRepository: DifficultyRepository, val chapterServiceImpl: ChapterServiceImpl) : CourseService {
    override fun get(id: Long): Course {
        return courseRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Course with id $id was not found") }
    }

    override fun getAll(): List<Course> {
        return courseRepository.findAll().toList()
    }

    override fun add(addRequest: CourseAddRequest): Long {
        val author = authorRepository.findById(addRequest.authorId)
        if (author.isEmpty) {
            throw ResourceNotFoundException("Author with id ${addRequest.authorId} was not found")
        }

        val difficulty = difficultyRepository.findById(addRequest.difficultyId)
        if (difficulty.isEmpty) {
            throw ResourceNotFoundException("Difficulty with id ${addRequest.difficultyId} was not found")
        }

        val course = Course(author.get(), difficulty.get(), addRequest.name, addRequest.shortDescription, addRequest.longDescription)
        return courseRepository.save(course).id ?: throw InternalErrorException("Could not save course")
    }

    // TODO this is stupid to get full course just to be sure that it exists, I should rather check exceptions
    override fun delete(id: Long) {
        val course = get(id)
        val courseId = course.id ?: throw InternalErrorException("Course from db should have had id $id but it is null")

        course.chapters.forEach{ _ -> chapterServiceImpl.delete(courseId)}

        courseRepository.deleteById(id)
    }

    override fun getCoursesHomepage(): List<CoursesHomepageOverview> {
        return courseRepository.findBy(CoursesHomepageOverview::class.java)
    }

    override fun getCourseOverview(id: Long): CourseOverview {
        try {
            return courseRepository.findById(id, CourseOverview::class.java)
        } catch (empty: EmptyResultDataAccessException) {
            throw ResourceNotFoundException("Course with id $id not found")
        }
    }

}