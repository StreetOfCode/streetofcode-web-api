package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.ChapterService
import sk.streetofcode.courseplatformbackend.api.dto.ChapterDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.api.request.ChapterEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.ChapterRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.model.Chapter
import sk.streetofcode.courseplatformbackend.model.toChapterDto
import java.lang.Exception
import java.time.OffsetDateTime

@Service
class ChapterServiceImpl(val chapterRepository: ChapterRepository, val courseRepository: CourseRepository, val lectureRepository: LectureRepository) : ChapterService {
    override fun get(id: Long): ChapterDto {
        return chapterRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Chapter with id $id was not found") }
            .toChapterDto()
    }

    override fun getAll(): List<ChapterDto> {
        return chapterRepository.findAll().map { it.toChapterDto() }.toList()
    }

    override fun getByCourseId(courseId: Long): List<ChapterDto> {
        return chapterRepository.findByCourseId(courseId).map { it.toChapterDto() }.toList()
    }

    override fun add(addRequest: ChapterAddRequest): ChapterDto {
        val course = courseRepository
            .findById(addRequest.courseId)
            .orElseThrow { ResourceNotFoundException("Course with id ${addRequest.courseId} was not found") }

        try {
            return chapterRepository
                .save(Chapter(course, addRequest.name, addRequest.chapterOrder))
                .toChapterDto()
        } catch (e: Exception) {
            throw InternalErrorException("Could not save chapter")
        }
    }

    override fun edit(id: Long, editRequest: ChapterEditRequest): ChapterDto {
        val existingChapter = chapterRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Chapter with id $id was not found") }

        if (id != editRequest.id) {
            throw BadRequestException("PathVariable id is not equal to request id field")
        } else {
            val chapter = existingChapter
            chapter.name = editRequest.name
            chapter.chapterOrder = editRequest.chapterOrder
            chapter.updatedAt = OffsetDateTime.now()
            return chapterRepository.save(chapter).toChapterDto()
        }
    }

    override fun delete(id: Long): ChapterDto {
        val chapter = chapterRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Chapter with id $id was not found") }

        // TODO shouldn't this be done by the database? CASCADE?
        // Remove all lectures if this course is removed
        lectureRepository.deleteByChapterId(id)

        chapterRepository.delete(chapter)
        return chapter.toChapterDto()
    }
}
