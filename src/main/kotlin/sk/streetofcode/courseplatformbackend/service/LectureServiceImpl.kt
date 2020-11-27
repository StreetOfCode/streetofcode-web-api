package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.LectureService
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.mapper.LectureMapper
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureEditRequest
import sk.streetofcode.courseplatformbackend.db.repository.ChapterRepository
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.model.Lecture
import java.time.OffsetDateTime

@Service
class LectureServiceImpl(val lectureRepository: LectureRepository, val chapterRepository: ChapterRepository, val mapper: LectureMapper) : LectureService {

    override fun get(id: Long): LectureDto {

        return mapper.toLectureDto(lectureRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Lecture with id $id was not found") })
    }

    override fun getAll(): List<LectureDto> {
        return lectureRepository.findAll().map { lecture -> mapper.toLectureDto(lecture) }.toList()
    }

    override fun getByChapterId(chapterId: Long): List<LectureDto> {
        return lectureRepository.findByChapterId(chapterId).map { lecture -> mapper.toLectureDto(lecture) }.toList()
    }

    override fun add(addRequest: LectureAddRequest): LectureDto {
        val chapter = chapterRepository.findById(addRequest.chapterId)
        if (chapter.isEmpty) {
            throw ResourceNotFoundException("Chapter with id ${addRequest.chapterId} was not found")
        }

        try {
            return mapper.toLectureDto(lectureRepository.save(Lecture(chapter.get(), addRequest.name, addRequest.lectureOrder, addRequest.content, addRequest.videoUrl)))
        } catch (e: Exception) {
            throw InternalErrorException("Could not save lecture")
        }
    }

    override fun edit(id: Long, editRequest: LectureEditRequest): LectureDto {
        val existingLecture = lectureRepository.findById(id)
        if (existingLecture.isEmpty) {
            throw ResourceNotFoundException("Lecture with id $id was not found")
        } else {
            if (id != editRequest.id) {
                throw BadRequestException("PathVariable id is not equal to request id field")
            } else {
                val lecture = existingLecture.get()
                lecture.name = editRequest.name
                lecture.content = editRequest.content
                lecture.videoUrl = editRequest.videoUrl
                lecture.lectureOrder = editRequest.lectureOrder
                lecture.updatedAt = OffsetDateTime.now()
                return mapper.toLectureDto(lectureRepository.save(lecture))
            }
        }
    }

    override fun delete(id: Long): LectureDto {
        val lecture = lectureRepository.findById(id)

        if (lecture.isPresent) {
            lectureRepository.deleteById(id)
            return mapper.toLectureDto(lecture.get())
        }
        else {
            throw ResourceNotFoundException("Lecture with id $id was not found")
        }
    }

}