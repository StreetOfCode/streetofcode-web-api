package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.LectureOrderSort
import sk.streetofcode.webapi.api.LectureService
import sk.streetofcode.webapi.api.dto.LectureDto
import sk.streetofcode.webapi.api.dto.LectureType
import sk.streetofcode.webapi.api.exception.AuthorizationException
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.LectureAddRequest
import sk.streetofcode.webapi.api.request.LectureEditRequest
import sk.streetofcode.webapi.client.vimeo.VimeoApiClient
import sk.streetofcode.webapi.db.repository.ChapterRepository
import sk.streetofcode.webapi.db.repository.CourseRepository
import sk.streetofcode.webapi.db.repository.LectureRepository
import sk.streetofcode.webapi.model.Lecture
import sk.streetofcode.webapi.model.toLectureDto
import java.time.OffsetDateTime

@Service
class LectureServiceImpl(
    val lectureRepository: LectureRepository,
    val chapterRepository: ChapterRepository,
    val courseRepository: CourseRepository,
    val vimeoApiClient: VimeoApiClient,
    val authenticationService: AuthenticationService,
    val courseProductService: CourseProductService
) : LectureService {

    companion object {
        private val log = LoggerFactory.getLogger(LectureServiceImpl::class.java)

        fun determineLectureType(lecture: Lecture): LectureType {
            return if (lecture.quiz != null && lecture.quiz!!.size > 0) {
                LectureType.QUIZ
            } else if (lecture.videoUrl != null) {
                LectureType.VIDEO
            } else {
                LectureType.TEXT
            }
        }
    }

    override fun get(id: Long): LectureDto {
        val lecture = lectureRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Lecture with id $id was not found") }

        // TODO paid-courses: allow if lecture preview is allowed
        if (!courseProductService.isOwnedByUser(lecture.chapter.course.id!!).isOwnedByUser
            // && !lecture.isPreviewAllowed
        ) {
            throw AuthorizationException("User does not own this course")
        }

        return lecture
            .toLectureDto()
    }

    override fun getAll(order: LectureOrderSort): List<LectureDto> {
        return lectureRepository.findAll(Sort.by(Sort.Direction.valueOf(order.name), "lectureOrder")).map { it.toLectureDto() }.toList()
    }

    override fun getByChapterId(chapterId: Long, order: LectureOrderSort): List<LectureDto> {
        return lectureRepository.findByChapterId(chapterId, Sort.by(Sort.Direction.valueOf(order.name), "lectureOrder")).map { it.toLectureDto() }.toList()
    }

    override fun add(addRequest: LectureAddRequest): LectureDto {
        val chapter = chapterRepository
            .findById(addRequest.chapterId)
            .orElseThrow { ResourceNotFoundException("Chapter with id ${addRequest.chapterId} was not found") }

        try {
            chapter.course.lecturesCount += 1
            return lectureRepository.save(
                Lecture(
                    chapter,
                    addRequest.name,
                    addRequest.lectureOrder,
                    addRequest.content,
                    addRequest.videoUrl,
                    vimeoApiClient.getVideoDurationInSeconds(addRequest.videoUrl),
                    addRequest.allowPreviewWhenPaid,
                )
            ).toLectureDto()
        } catch (e: Exception) {
            // Check this because of youtubeApiClient
            if (e is InternalErrorException) {
                throw e
            }
            log.error("Problem with saving lecture to db", e)
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
            existingLecture.name = editRequest.name
            existingLecture.content = editRequest.content
            existingLecture.videoUrl = editRequest.videoUrl
            existingLecture.videoDurationSeconds = vimeoApiClient.getVideoDurationInSeconds(editRequest.videoUrl)
            existingLecture.lectureOrder = editRequest.lectureOrder
            existingLecture.updatedAt = OffsetDateTime.now()
            existingLecture.allowPreviewWhenPaid = editRequest.allowPreviewWhenPaid
            return lectureRepository.save(existingLecture).toLectureDto()
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
