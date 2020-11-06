package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.ChapterService
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.api.request.ChapterEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.ChapterRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.model.Chapter
import java.time.OffsetDateTime

@Service
class ChapterServiceImpl(val chapterRepository: ChapterRepository, val courseRepository: CourseRepository, val lectureRepository: LectureRepository) : ChapterService {
    override fun get(id: Long): Chapter {
        return chapterRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Chapter with id $id was not found") }
    }

    override fun getAll(): List<Chapter> {
        return chapterRepository.findAll().toList()
    }

    override fun getByCourseId(courseId: Long): List<Chapter> {
        return chapterRepository.findByCourseId(courseId)
    }

    override fun add(addRequest: ChapterAddRequest): Long {
        val course = courseRepository.findById(addRequest.courseId)
        if (course.isEmpty) {
            throw ResourceNotFoundException("Course with id ${addRequest.courseId} was not found")
        }

        val chapter = Chapter(course.get(), addRequest.name, addRequest.chapterOrder)
        return chapterRepository.save(chapter).id ?: throw InternalErrorException("Could not save chapter")
    }

    override fun edit(id: Long, editRequest: ChapterEditRequest): Chapter {
        val existingChapter = chapterRepository.findById(id)
        if (existingChapter.isEmpty) {
            throw ResourceNotFoundException("Chapter with id $id was not found")
        } else {
            if (id != editRequest.id) {
                throw BadRequestException("PathVariable id is not equal to request id field")
            } else {
                val chapter = existingChapter.get()
                chapter.name = editRequest.name
                chapter.chapterOrder = editRequest.chapterOrder
                chapter.updatedAt = OffsetDateTime.now()
                return chapterRepository.save(chapter)
            }
        }
    }

    override fun delete(id: Long) {
        if (chapterRepository.existsById(id)) {
            // Remove all lectures if this course is removed
            lectureRepository.deleteByChapterId(id)

            chapterRepository.deleteById(id)
        } else {
            throw ResourceNotFoundException("Chapter with id $id was not found")
        }
    }

}