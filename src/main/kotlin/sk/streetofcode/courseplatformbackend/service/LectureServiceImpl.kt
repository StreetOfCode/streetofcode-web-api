package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.LectureService
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureEditRequest
import sk.streetofcode.courseplatformbackend.client.youtube.YoutubeApiClient
import sk.streetofcode.courseplatformbackend.db.repository.ChapterRepository
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.model.Lecture
import sk.streetofcode.courseplatformbackend.model.toLectureDto
import java.time.OffsetDateTime

@Service
class LectureServiceImpl(
    val lectureRepository: LectureRepository,
    val chapterRepository: ChapterRepository,
    val courseRepository: CourseRepository,
    val youtubeApiClient: YoutubeApiClient
) : LectureService {

    override fun get(id: Long): LectureDto {

        return lectureRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Lecture with id $id was not found") }
            .toLectureDto()
    }

    override fun getAll(): List<LectureDto> {
        return lectureRepository.findAll().map { it.toLectureDto() }.toList()
    }

    override fun getByChapterId(chapterId: Long): List<LectureDto> {
        return lectureRepository.findByChapterId(chapterId).map { it.toLectureDto() }.toList()
    }

    override fun add(addRequest: LectureAddRequest): LectureDto {
        val chapter = chapterRepository
            .findById(addRequest.chapterId)
            .orElseThrow { ResourceNotFoundException("Chapter with id ${addRequest.chapterId} was not found") }

        try {
            val updatedChapter = chapter
            updatedChapter.course.lecturesCount += 1
            return lectureRepository.save(
                Lecture(
                    updatedChapter,
                    addRequest.name,
                    addRequest.lectureOrder,
                    addRequest.content,
                    addRequest.videoUrl,
                    youtubeApiClient.getVideoDurationInSeconds(addRequest.videoUrl)
                )
            ).toLectureDto()
        } catch (e: Exception) {
            if (e is InternalErrorException) {
                throw e
            }
            throw InternalErrorException("Could not save lecture")
        }
    }

    override fun edit(id: Long, editRequest: LectureEditRequest): LectureDto {
        val existingLecture = lectureRepository
            .findById(id)
            .orElseThrow { throw ResourceNotFoundException("Lecture with id $id was not found") }

        if (id != editRequest.id) {
            throw BadRequestException("PathVariable id is not equal to request id field")
        } else {
            val lecture = existingLecture
            lecture.name = editRequest.name
            lecture.content = editRequest.content
            lecture.videoUrl = editRequest.videoUrl
            lecture.videoDurationSeconds = youtubeApiClient.getVideoDurationInSeconds(editRequest.videoUrl)
            lecture.lectureOrder = editRequest.lectureOrder
            lecture.updatedAt = OffsetDateTime.now()
            return lectureRepository.save(lecture).toLectureDto()
        }
    }

    override fun delete(id: Long): LectureDto {
        val lecture = lectureRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Lecture with id $id was not found") }

        lectureRepository.deleteById(id)
        courseRepository.updateLecturesCount(-1, lecture.chapter.course.id!!)
        return lecture.toLectureDto()
    }
}
