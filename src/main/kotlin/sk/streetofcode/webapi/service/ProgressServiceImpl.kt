package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.ChapterOrderSort
import sk.streetofcode.webapi.api.ChapterService
import sk.streetofcode.webapi.api.CourseService
import sk.streetofcode.webapi.api.LectureService
import sk.streetofcode.webapi.api.ProgressService
import sk.streetofcode.webapi.api.dto.LectureDto
import sk.streetofcode.webapi.api.dto.progress.ChapterProgressOverviewDto
import sk.streetofcode.webapi.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.webapi.api.dto.progress.LectureProgressOverviewDto
import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.ResetProgressDto
import sk.streetofcode.webapi.db.repository.CourseRepository
import sk.streetofcode.webapi.db.repository.progress.ProgressLectureRepository
import sk.streetofcode.webapi.db.repository.progress.UserProgressMetadataRepository
import sk.streetofcode.webapi.model.Course
import sk.streetofcode.webapi.model.progress.ProgressLecture
import sk.streetofcode.webapi.model.progress.ProgressStatus
import sk.streetofcode.webapi.model.progress.UserProgressMetadata
import sk.streetofcode.webapi.model.progress.toUserProgressMetadataDto
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit
import javax.transaction.Transactional

@Service
class ProgressServiceImpl(
    private val progressMetadataRepository: UserProgressMetadataRepository,
    private val progressLectureRepository: ProgressLectureRepository,
    private val lectureService: LectureService,
    private val chapterService: ChapterService,
    private val courseService: CourseService,
    private val courseRepository: CourseRepository
) : ProgressService {

    companion object {
        private val log = LoggerFactory.getLogger(ProgressServiceImpl::class.java)
    }

    @Transactional
    @Synchronized
    override fun updateProgress(userId: String, lectureId: Long): CourseProgressOverviewDto {
        // TODO check if course is paid and if it is then check if user owns the course?

        val lecture = lectureService.get(lectureId)
        val courseId = lecture.course.id

        // if this lecture is already viewed then don't save it again
        if (progressLectureRepository.findByUserIdAndLectureId(userId, lectureId).isPresent) {
            return this.getProgressOverview(userId, courseId)
        }

        val courseLecturesCount = lecture.course.lecturesCount

        // update progress lecture
        progressLectureRepository.save(ProgressLecture(userId, lectureId))

        // update or create progress metadata
        val maybeProgressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
        if (maybeProgressMetadata.isPresent) {
            val progressMetadata = maybeProgressMetadata.get()
            updateProgressMetadata(progressMetadata, 1, courseLecturesCount)
        } else {
            progressMetadataRepository.save(UserProgressMetadata(userId, courseId, 1))
        }

        return this.getProgressOverview(userId, courseId)
    }

    @Transactional
    @Synchronized
    override fun resetProgress(userId: String, resetProgressDto: ResetProgressDto): CourseProgressOverviewDto {
        return when {
            resetProgressDto.lectureId != null -> {
                resetLectureProgress(userId, resetProgressDto.lectureId)
            }
            resetProgressDto.chapterId != null -> {
                resetChapterProgress(userId, resetProgressDto.chapterId)
            }
            resetProgressDto.courseId != null -> {
                resetCourseProgress(userId, resetProgressDto.courseId)
            }
            else -> {
                throw BadRequestException("At least one field in resetProgressDto has to be present")
            }
        }
    }

    override fun getProgressOverview(userId: String, courseId: Long): CourseProgressOverviewDto {
        val course = courseRepository.findById(courseId)
            .orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        val allLecturesViewedFromAllCourses =
            progressLectureRepository.findAllByUserId(userId).map { progress -> progress.lectureId }.toSet()

        val chapters = course.chapters.map { courseChapter ->
            ChapterProgressOverviewDto(
                id = courseChapter.id!!,
                name = courseChapter.name,
                viewed = allLecturesViewedFromAllCourses.containsAll(courseChapter.lectures.map { lecture -> lecture.id!! }),
                chapterDurationMinutes = courseChapter.lectures.sumOf { lecture ->
                    TimeUnit.SECONDS.toMinutes(lecture.videoDurationSeconds.toLong()).toInt()
                },
                lectures = courseChapter.lectures.map { chapterLecture ->
                    LectureProgressOverviewDto(
                        id = chapterLecture.id!!,
                        name = chapterLecture.name,
                        viewed = allLecturesViewedFromAllCourses.contains(chapterLecture.id),
                        videoDurationSeconds = chapterLecture.videoDurationSeconds,
                        lectureType = LectureServiceImpl.determineLectureType(chapterLecture),
                    )
                }
            )
        }

        return CourseProgressOverviewDto(
            lecturesViewed = chapters.flatMap { chapter -> chapter.lectures }.filter { lecture -> lecture.viewed }
                .count(),
            courseLecturesCount = course.lecturesCount,
            chapters = chapters
        )
    }

    override fun getUserProgressMetadata(userId: String, courseId: Long): UserProgressMetadataDto {
        val metadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }
        val courseLecturesCount = courseService.get(courseId).lecturesCount

        val course = courseRepository.findById(courseId)
            .orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        val firstUnseenLecture = getFirstUnseenLecture(userId, course)

        return metadata.toUserProgressMetadataDto(
            courseLecturesCount,
            firstUnseenLecture?.chapter?.id,
            firstUnseenLecture?.id
        )
    }

    override fun getUserProgressMetadataOrNull(userId: String, courseId: Long): UserProgressMetadataDto? {
        return try {
            getUserProgressMetadata(userId, courseId)
        } catch (e: ResourceNotFoundException) {
            null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getStartedCourseIds(userId: String): List<Long> {
        return progressMetadataRepository.getStartedCourseIds(userId)
    }

    private fun resetLectureProgress(userId: String, lectureId: Long): CourseProgressOverviewDto {
        val lecture = lectureService.get(lectureId)
        val courseId = lecture.course.id

        val progressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }

        // remove progress lecture
        progressLectureRepository.deleteByUserIdAndLectureId(userId, lectureId)

        // update progress metadata
        updateProgressMetadata(progressMetadata, -1, lecture.course.lecturesCount)

        return this.getProgressOverview(userId, courseId)
    }

    private fun resetChapterProgress(userId: String, chapterId: Long): CourseProgressOverviewDto {
        val chapter = chapterService.get(chapterId)
        val courseId = chapter.course.id
        val lectureIds = chapter.lectures.map { lectureDto -> lectureDto.id }

        val progressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }

        // remove progress lectures
        progressLectureRepository.deleteByUserIdAndLectureIdIn(userId, lectureIds)

        // update progress metadata
        updateProgressMetadata(progressMetadata, -lectureIds.size, chapter.course.lecturesCount)

        return this.getProgressOverview(userId, courseId)
    }

    private fun resetCourseProgress(userId: String, courseId: Long): CourseProgressOverviewDto {
        val lectureIds =
            chapterService.getByCourseId(courseId, ChapterOrderSort.ASC).flatMap { chapterDto -> chapterDto.lectures }
                .map { lectureDto -> lectureDto.id }

        val progressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }

        // remove progress lecture
        progressLectureRepository.deleteByUserIdAndLectureIdIn(userId, lectureIds)

        // update progress metadata
        updateProgressMetadata(progressMetadata, -progressMetadata.lecturesViewed, lectureIds.size) // reset to 0

        return this.getProgressOverview(userId, courseId)
    }

    private fun updateIsCourseFinished(progressMetadata: UserProgressMetadata, courseLecturesCount: Int) {
        val shouldUnfinish =
            progressMetadata.status == ProgressStatus.FINISHED && progressMetadata.lecturesViewed < courseLecturesCount
        if (shouldUnfinish) {
            progressMetadata.status = ProgressStatus.IN_PROGRESS
            progressMetadata.finishedAt = null
        }

        val shouldFinish = progressMetadata.lecturesViewed == courseLecturesCount
        if (shouldFinish) {
            progressMetadata.status = ProgressStatus.FINISHED
            progressMetadata.finishedAt = OffsetDateTime.now()
        }
    }

    private fun updateProgressMetadata(
        progressMetadata: UserProgressMetadata,
        lecturesViewedDelta: Int,
        courseLecturesCount: Int
    ) {
        val updatedLecturesViewed = progressMetadata.lecturesViewed + lecturesViewedDelta
        if (updatedLecturesViewed == 0) {
            progressMetadataRepository.delete(progressMetadata)
        } else if (updatedLecturesViewed in 1..courseLecturesCount) {
            progressMetadata.lastUpdatedAt = OffsetDateTime.now()
            progressMetadata.lecturesViewed = updatedLecturesViewed
            updateIsCourseFinished(progressMetadata, courseLecturesCount)
            progressMetadataRepository.save(progressMetadata)
        } else {
            log.error(
                "Something is terribly wrong in updating progress metadata, because apparently " +
                        "new updatedLecturesViewed $updatedLecturesViewed is out of bound. Course has total of $courseLecturesCount" +
                        ". Progress may be corrupted."
            )
            throw InternalErrorException("Problem in internal logic of updating progress metadata")
        }
    }

    private fun getFirstUnseenLecture(userId: String, course: Course): LectureDto? {
        val courseLectureIds = course.chapters
            .flatMap { chapter -> chapter.lectures.map { lecture -> lecture.id!! } }
        val seenLectureIds = progressLectureRepository.findAllByUserId(userId)
            .map { lecture -> lecture.lectureId }
            .toSet()

        return courseLectureIds
            .asSequence()
            .filter { !seenLectureIds.contains(it) }
            .map { lectureId -> lectureService.get(lectureId) }
            .firstOrNull()
    }
}
