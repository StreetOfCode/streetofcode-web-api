package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.ChapterService
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.LectureService
import sk.streetofcode.courseplatformbackend.api.ProgressService
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.api.dto.progress.ChapterProgressOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.progress.LectureProgressOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.courseplatformbackend.api.exception.BadRequestException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.ResetProgressDto
import sk.streetofcode.courseplatformbackend.db.repository.CourseRepository
import sk.streetofcode.courseplatformbackend.db.repository.progress.ProgressLectureRepository
import sk.streetofcode.courseplatformbackend.db.repository.progress.UserProgressMetadataRepository
import sk.streetofcode.courseplatformbackend.model.Course
import sk.streetofcode.courseplatformbackend.model.progress.ProgressLecture
import sk.streetofcode.courseplatformbackend.model.progress.ProgressStatus
import sk.streetofcode.courseplatformbackend.model.progress.UserProgressMetadata
import sk.streetofcode.courseplatformbackend.model.progress.toUserProgressMetadataDto
import java.time.OffsetDateTime
import java.util.UUID

@Service
class ProgressServiceImpl(
    private val progressMetadataRepository: UserProgressMetadataRepository,
    private val progressLectureRepository: ProgressLectureRepository,
    private val lectureService: LectureService,
    private val chapterService: ChapterService,
    private val courseService: CourseService,
    private val courseRepository: CourseRepository
) : ProgressService {

    override fun updateProgress(userId: UUID, lectureId: Long) {
        // if this lecture is already viewed then don't save it again
        if (progressLectureRepository.findByUserIdAndLectureId(userId, lectureId).isPresent) {
            return
        }
        val lecture = lectureService.get(lectureId)
        val courseId = lecture.course.id
        val courseLecturesCount = lecture.course.lecturesCount

        // update progress lecture
        progressLectureRepository.save(ProgressLecture(userId, lectureId))

        // update or create progress metadata
        val maybeProgressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
        if (maybeProgressMetadata.isPresent) {
            val progressMetadata = maybeProgressMetadata.get()
            updateProgressMetadata(progressMetadata, 1)
            maybeFinishCourse(progressMetadata, courseLecturesCount)
            progressMetadataRepository.save(progressMetadata)
        } else {
            progressMetadataRepository.save(UserProgressMetadata(userId, courseId, 1))
        }
    }

    override fun resetProgress(userId: UUID, resetProgressDto: ResetProgressDto) {
        when {
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

    override fun getProgressOverview(userId: UUID, courseId: Long): CourseProgressOverviewDto {
        val course = courseRepository.findById(courseId)
            .orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        val allLecturesViewedFromAllCourses =
            progressLectureRepository.findAllByUserId(userId).map { progress -> progress.lectureId }.toSet()

        val chapters = course.chapters.map { courseChapter ->
            ChapterProgressOverviewDto(
                id = courseChapter.id!!,
                name = courseChapter.name,
                viewed = allLecturesViewedFromAllCourses.containsAll(courseChapter.lectures.map { lecture -> lecture.id!! }),
                lectures = courseChapter.lectures.map { chapterLecture ->
                    LectureProgressOverviewDto(
                        id = chapterLecture.id!!,
                        name = chapterLecture.name,
                        viewed = allLecturesViewedFromAllCourses.contains(chapterLecture.id)
                    )
                }
            )
        }

        return CourseProgressOverviewDto(
            lecturesViewed = chapters.flatMap { chapter -> chapter.lectures }.filter { lecture -> lecture.viewed }.count(),
            courseLecturesCount = course.lecturesCount,
            chapters = chapters
        )
    }

    override fun getUserProgressMetadata(userId: UUID, courseId: Long): UserProgressMetadataDto {
        val metadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }
        val courseLecturesCount = courseService.get(courseId).lecturesCount

        val course = courseRepository.findById(courseId)
            .orElseThrow { ResourceNotFoundException("Course with id $courseId was not found") }

        val firstUnseenLecture = getFirstUnseenLecture(userId, course)

        return metadata.toUserProgressMetadataDto(courseLecturesCount, firstUnseenLecture?.chapter?.id, firstUnseenLecture?.id)
    }

    override fun getUserProgressMetadataOrNull(userId: UUID, courseId: Long): UserProgressMetadataDto? {
        return try {
            getUserProgressMetadata(userId, courseId)
        } catch (e: ResourceNotFoundException) {
            null
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getStartedCourseIds(userId: UUID): List<Long> {
        return progressMetadataRepository.getStartedCourseIds(userId)
    }

    private fun resetLectureProgress(userId: UUID, lectureId: Long) {
        val lecture = lectureService.get(lectureId)
        val courseId = lecture.course.id

        // remove progress lecture
        progressLectureRepository.deleteByUserIdAndLectureId(userId, lectureId)

        // update progress metadata
        val progressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }
        updateProgressMetadata(progressMetadata, -1)
        maybeResetFinishCourse(progressMetadata)

        progressMetadataRepository.save(progressMetadata)
    }

    private fun resetChapterProgress(userId: UUID, chapterId: Long) {
        val chapter = chapterService.get(chapterId)
        val courseId = chapter.course.id
        val lectureIds = chapter.lectures.map { lectureDto -> lectureDto.id }
        // remove progress lectures
        progressLectureRepository.deleteByUserIdAndLectureIdIn(userId, lectureIds)

        // update progress metadata
        val progressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }
        updateProgressMetadata(progressMetadata, -lectureIds.size)
        maybeResetFinishCourse(progressMetadata)
        progressMetadataRepository.save(progressMetadata)
    }

    private fun resetCourseProgress(userId: UUID, courseId: Long) {
        val lectureIds = chapterService.getByCourseId(courseId).flatMap { chapterDto -> chapterDto.lectures }
            .map { lectureDto -> lectureDto.id }

        // remove progress lecture
        progressLectureRepository.deleteByUserIdAndLectureIdIn(userId, lectureIds)

        // update progress metadata
        val progressMetadata = progressMetadataRepository.findByUserIdAndCourseId(userId, courseId)
            .orElseThrow { ResourceNotFoundException("Not found progress metadata for userId $userId and courseId $courseId") }
        updateProgressMetadata(progressMetadata, -progressMetadata.lecturesViewed) // reset to 0
        maybeResetFinishCourse(progressMetadata)

        progressMetadataRepository.save(progressMetadata)
    }

    private fun maybeResetFinishCourse(progressMetadata: UserProgressMetadata) {
        if (progressMetadata.status == ProgressStatus.FINISHED) {
            progressMetadata.status = ProgressStatus.IN_PROGRESS
            progressMetadata.finishedAt = null
        }
    }

    private fun maybeFinishCourse(progressMetadata: UserProgressMetadata, courseLecturesCount: Int) {
        if (progressMetadata.lecturesViewed == courseLecturesCount) {
            progressMetadata.status = ProgressStatus.FINISHED
            progressMetadata.finishedAt = OffsetDateTime.now()
        }
    }

    private fun updateProgressMetadata(progressMetadata: UserProgressMetadata, lecturesViewedDelta: Int) {
        progressMetadata.lastUpdatedAt = OffsetDateTime.now()
        progressMetadata.lecturesViewed = progressMetadata.lecturesViewed + lecturesViewedDelta
        // todo log error if lecturesViewed is out of boundary (0, courseLecturesCount).. just to be sure
    }

    private fun getFirstUnseenLecture(userId: UUID, course: Course): LectureDto? {
        val courseLectureIds = course.chapters
            .flatMap { chapter -> chapter.lectures.map { lecture -> lecture.id!! } }
        val seenLectureIds = progressLectureRepository.findAllByUserId(userId)
            .map { lecture -> lecture.id }
            .toSet()

        return courseLectureIds
            .asSequence()
            .filter { !seenLectureIds.contains(it) }
            .map { lectureId -> lectureService.get(lectureId) }
            .firstOrNull()
    }
}
