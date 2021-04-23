package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.ChapterService
import sk.streetofcode.courseplatformbackend.api.dto.ChapterDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.mapper.ChapterMapper
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.api.request.ChapterEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.ChapterRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.model.Chapter
import java.lang.Exception
import java.time.OffsetDateTime

@Service
class ChapterServiceImpl(val chapterRepository: ChapterRepository, val courseRepository: CourseRepository, val lectureRepository: LectureRepository, val mapper: ChapterMapper) : ChapterService {
    override fun get(id: Long): ChapterDto {
        return mapper.toChapterDto(
            chapterRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Chapter with id $id was not found") }
        )
    }

    override fun getAll(): List<ChapterDto> {
        return chapterRepository.findAll().map { chapter -> mapper.toChapterDto(chapter) }.toList()
    }

    override fun getByCourseId(courseId: Long): List<ChapterDto> {
        return chapterRepository.findByCourseId(courseId).map { chapter -> mapper.toChapterDto(chapter) }.toList()
    }

    override fun add(addRequest: ChapterAddRequest): ChapterDto {
        val course = courseRepository.findById(addRequest.courseId)
        if (course.isEmpty) {
            throw ResourceNotFoundException("Course with id ${addRequest.courseId} was not found")
        }

        try {
            return mapper.toChapterDto(chapterRepository.save(Chapter(course.get(), addRequest.name, addRequest.chapterOrder)))
        } catch (e: Exception) {
            throw InternalErrorException("Could not save chapter")
        }
    }

    override fun edit(id: Long, editRequest: ChapterEditRequest): ChapterDto {
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
                return mapper.toChapterDto(chapterRepository.save(chapter))
            }
        }
    }

    override fun delete(id: Long): ChapterDto {
        val chapter = chapterRepository.findById(id)
        if (chapter.isPresent) {
            // Remove all lectures if this course is removed
            lectureRepository.deleteByChapterId(id)

            chapterRepository.delete(chapter.get())
            return mapper.toChapterDto(chapter.get())
        } else {
            throw ResourceNotFoundException("Chapter with id $id was not found")
        }
    }
}
