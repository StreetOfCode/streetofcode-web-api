package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.webapi.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.ResetProgressDto
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation
import sk.streetofcode.webapi.model.progress.ProgressStatus

@SpringBootTestAnnotation
class ProgressIntegrationTests : IntegrationTests() {

    init {
        "complex progress tracking test" {
            val courseId = 1L

            // progress metadata is not found yet
            getProgressMetadataNotFound(courseId)

            // check progressOverview - everything is not seen yet
            val progressOverview = getProgressOverview(courseId)
            progressOverview.lecturesViewed shouldBe 0
            progressOverview.chapters.forEach { it ->
                it.viewed shouldBe false
                it.lectures.forEach { it.viewed shouldBe false }
            }

            // update progress, seen lectureId 1
            updateProgress(1)

            // verify seen lecture
            val progressOverviewLectureSeen = getProgressOverview(courseId)
            progressOverviewLectureSeen.lecturesViewed shouldBe 1
            progressOverviewLectureSeen.chapters[0].viewed shouldBe false
            progressOverviewLectureSeen.chapters[0].lectures[0].viewed shouldBe true
            progressOverviewLectureSeen.chapters[0].lectures[1].viewed shouldBe false

            // verify progress metadata - lecture seen
            val progressMetadataLectureSeen = getProgressMetadata(courseId)
            progressMetadataLectureSeen.lecturesViewed shouldBe 1
            progressMetadataLectureSeen.status shouldBe ProgressStatus.IN_PROGRESS
            progressMetadataLectureSeen.finishedAt shouldBe null
            progressMetadataLectureSeen.nextChapterId shouldBe 1
            progressMetadataLectureSeen.nextLectureId shouldBe 2

            // update progress, seen lectureId 2, 3
            updateProgress(2)
            updateProgress(3)

            // verify seen chapter
            val progressOverviewChapterSeen = getProgressOverview(courseId)
            progressOverviewChapterSeen.lecturesViewed shouldBe 3
            progressOverviewChapterSeen.chapters[0].viewed shouldBe true
            progressOverviewChapterSeen.chapters[0].lectures[0].viewed shouldBe true
            progressOverviewChapterSeen.chapters[0].lectures[1].viewed shouldBe true
            progressOverviewChapterSeen.chapters[0].lectures[2].viewed shouldBe true
            progressOverviewChapterSeen.chapters[1].viewed shouldBe false

            // verify progress metadata - chapter seen
            val progressMetadataChapterSeen = getProgressMetadata(courseId)
            progressMetadataChapterSeen.lecturesViewed shouldBe 3
            progressMetadataChapterSeen.status shouldBe ProgressStatus.IN_PROGRESS
            progressMetadataChapterSeen.finishedAt shouldBe null
            progressMetadataChapterSeen.nextChapterId shouldBe 2
            progressMetadataChapterSeen.nextLectureId shouldBe 4

            // reset progress lecture
            resetProgress(ResetProgressDto(lectureId = 3))

            // verify changed progress overview - chapter is not seen, lecture 1,2 are seen
            val progressOverviewLectureReset = getProgressOverview(courseId)
            progressOverviewLectureReset.lecturesViewed shouldBe 2
            progressOverviewLectureReset.chapters[0].viewed shouldBe false
            progressOverviewLectureReset.chapters[0].lectures[0].viewed shouldBe true
            progressOverviewLectureReset.chapters[0].lectures[1].viewed shouldBe true
            progressOverviewLectureReset.chapters[0].lectures[2].viewed shouldBe false

            // update progress - lecture 3
            updateProgress(3)

            // verify changed progress overview - chapter is seen again
            val progressOverviewChapterSeenAgain = getProgressOverview(courseId)
            progressOverviewChapterSeenAgain.lecturesViewed shouldBe 3
            progressOverviewChapterSeenAgain.chapters[0].viewed shouldBe true
            progressOverviewChapterSeenAgain.chapters[0].lectures[0].viewed shouldBe true
            progressOverviewChapterSeenAgain.chapters[0].lectures[1].viewed shouldBe true
            progressOverviewChapterSeenAgain.chapters[0].lectures[2].viewed shouldBe true

            // reset progress chapter
            resetProgress(ResetProgressDto(chapterId = 1))

            // verify that everything is unseen again
            val progressOverviewChapterReset = getProgressOverview(courseId)
            progressOverviewChapterReset.lecturesViewed shouldBe 0
            progressOverviewChapterReset.chapters.forEach { it ->
                it.viewed shouldBe false
                it.lectures.forEach { it.viewed shouldBe false }
            }

            // verify progress metadata - nothing seen
            val progressMetadataReset = getProgressMetadata(courseId)
            progressMetadataReset.lecturesViewed shouldBe 0
            progressMetadataReset.status shouldBe ProgressStatus.IN_PROGRESS
            progressMetadataReset.finishedAt shouldBe null
            progressMetadataReset.nextChapterId shouldBe 1
            progressMetadataReset.nextLectureId shouldBe 1

            // update progress - see whole course
            for (i in 1..5) {
                updateProgress(i.toLong())
            }

            // verify changed progress overview - whole course seen
            val progressOverviewCourseSeen = getProgressOverview(courseId)
            progressOverviewCourseSeen.lecturesViewed shouldBe 5
            progressOverviewCourseSeen.chapters.forEach { it ->
                it.viewed shouldBe true
                it.lectures.forEach { it.viewed shouldBe true }
            }

            // verify progress metadata - course seen
            val progressMetadataCourseSeen = getProgressMetadata(courseId)
            progressMetadataCourseSeen.lecturesViewed shouldBe 5
            progressMetadataCourseSeen.status shouldBe ProgressStatus.FINISHED
            progressMetadataCourseSeen.finishedAt shouldNotBe null

            // reset progress course
            resetProgress(ResetProgressDto(courseId = 1))

            // verify progress overview - reset course
            val progressOverviewCourseReset = getProgressOverview(courseId)
            progressOverviewCourseReset.lecturesViewed shouldBe 0
            progressOverviewCourseReset.chapters.forEach { it ->
                it.viewed shouldBe false
                it.lectures.forEach { it.viewed shouldBe false }
            }

            // verify progress metadata - nothing seen
            val progressMetadataResetAgain = getProgressMetadata(courseId)
            progressMetadataResetAgain.lecturesViewed shouldBe 0
            progressMetadataResetAgain.status shouldBe ProgressStatus.IN_PROGRESS
            progressMetadataResetAgain.finishedAt shouldBe null
            progressMetadataResetAgain.nextChapterId shouldBe 1
            progressMetadataResetAgain.nextLectureId shouldBe 1
        }

        "fail reset progress, bad request" {
            resetProgressBadRequest(ResetProgressDto()) // everything is null in request
        }
    }

    private fun getProgressOverview(courseId: Long): CourseProgressOverviewDto {
        return restWithAdminRole().getForEntity<CourseProgressOverviewDto>("/progress/overview/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getProgressMetadata(courseId: Long): UserProgressMetadataDto {
        return restWithAdminRole().getForEntity<UserProgressMetadataDto>("/progress/metadata/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getProgressMetadataNotFound(courseId: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/progress/metadata/$courseId").statusCode shouldBe HttpStatus.NOT_FOUND
    }

    private fun updateProgress(lectureId: Long) {
        return restWithAdminRole().postForEntity<Void>("/progress/update/$lectureId").statusCode shouldBe HttpStatus.OK
    }

    private fun resetProgress(body: ResetProgressDto) {
        return restWithAdminRole().postForEntity<Void>("/progress/reset", body).statusCode shouldBe HttpStatus.OK
    }

    private fun resetProgressBadRequest(body: ResetProgressDto) {
        return restWithAdminRole().postForEntity<BadRequestException>("/progress/reset", body).statusCode shouldBe HttpStatus.BAD_REQUEST
    }
}
