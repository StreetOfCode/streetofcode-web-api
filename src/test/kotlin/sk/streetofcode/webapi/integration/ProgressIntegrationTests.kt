package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.webapi.api.dto.ChapterDto
import sk.streetofcode.webapi.api.dto.CourseDto
import sk.streetofcode.webapi.api.dto.LectureDto
import sk.streetofcode.webapi.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.api.exception.BadRequestException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.ChapterAddRequest
import sk.streetofcode.webapi.api.request.CourseAddRequest
import sk.streetofcode.webapi.api.request.LectureAddRequest
import sk.streetofcode.webapi.api.request.ResetProgressDto
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation
import sk.streetofcode.webapi.model.CourseStatus
import sk.streetofcode.webapi.model.progress.ProgressStatus

@SpringBootTestAnnotation
class ProgressIntegrationTests : IntegrationTests() {

    init {
        "complex progress tracking test" {
            val courseId = createCourseForComplexProgressTest()

            // progress metadata is not found yet
            getProgressMetadataNotFound(courseId)

            // check progressOverview - everything is not seen yet
            val initialProgressOverview = getProgressOverview(courseId)
            initialProgressOverview.lecturesViewed shouldBe 0
            initialProgressOverview.chapters.forEach { it ->
                it.viewed shouldBe false
                it.lectures.forEach { it.viewed shouldBe false }
            }

            // update progress, seen first lecture
            updateProgress(initialProgressOverview.chapters[0].lectures[0].id)

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
            progressMetadataLectureSeen.nextChapterId shouldBe initialProgressOverview.chapters[0].id
            progressMetadataLectureSeen.nextLectureId shouldBe initialProgressOverview.chapters[0].lectures[1].id

            // update progress, seen rest of the lectures in first chapter
            updateProgress(initialProgressOverview.chapters[0].lectures[1].id)
            updateProgress(initialProgressOverview.chapters[0].lectures[2].id)

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
            progressMetadataChapterSeen.nextChapterId shouldBe initialProgressOverview.chapters[1].id
            progressMetadataChapterSeen.nextLectureId shouldBe initialProgressOverview.chapters[1].lectures[0].id

            // reset progress lecture
            resetProgress(ResetProgressDto(lectureId = initialProgressOverview.chapters[0].lectures[2].id))

            // verify changed progress overview - chapter is not seen, first and second lectures are seen
            val progressOverviewLectureReset = getProgressOverview(courseId)
            progressOverviewLectureReset.lecturesViewed shouldBe 2
            progressOverviewLectureReset.chapters[0].viewed shouldBe false
            progressOverviewLectureReset.chapters[0].lectures[0].viewed shouldBe true
            progressOverviewLectureReset.chapters[0].lectures[1].viewed shouldBe true
            progressOverviewLectureReset.chapters[0].lectures[2].viewed shouldBe false

            // update progress - see last lecture in first chapter
            updateProgress(initialProgressOverview.chapters[0].lectures[2].id)

            // verify changed progress overview - chapter is seen again
            val progressOverviewChapterSeenAgain = getProgressOverview(courseId)
            progressOverviewChapterSeenAgain.lecturesViewed shouldBe 3
            progressOverviewChapterSeenAgain.chapters[0].viewed shouldBe true
            progressOverviewChapterSeenAgain.chapters[0].lectures[0].viewed shouldBe true
            progressOverviewChapterSeenAgain.chapters[0].lectures[1].viewed shouldBe true
            progressOverviewChapterSeenAgain.chapters[0].lectures[2].viewed shouldBe true

            // reset progress chapter
            resetProgress(ResetProgressDto(chapterId = initialProgressOverview.chapters[0].id))

            // verify that everything is unseen again
            val progressOverviewChapterReset = getProgressOverview(courseId)
            progressOverviewChapterReset.lecturesViewed shouldBe 0
            progressOverviewChapterReset.chapters.forEach { it ->
                it.viewed shouldBe false
                it.lectures.forEach { it.viewed shouldBe false }
            }

            // verify progress metadata - empty data should not exist
            getProgressMetadataNotFound(courseId)

            // update progress - see whole course
            for (i in initialProgressOverview.chapters[0].lectures[0].id..initialProgressOverview.chapters[1].lectures[1].id) {
                updateProgress(i)
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
            resetProgress(ResetProgressDto(courseId))

            // verify progress overview - reset course
            val progressOverviewCourseReset = getProgressOverview(courseId)
            progressOverviewCourseReset.lecturesViewed shouldBe 0
            progressOverviewCourseReset.chapters.forEach { it ->
                it.viewed shouldBe false
                it.lectures.forEach { it.viewed shouldBe false }
            }

            // verify progress metadata - empty data should not exist
            getProgressMetadataNotFound(courseId)
        }

        "fail reset progress, bad request" {
            resetProgressBadRequest(ResetProgressDto()) // everything is null in request
        }
    }

    // Creates course with 2 chapters and 5 lectures (3 lectures in chapter 1 and 2 lectures in chapter 2)
    // Returns course id
    private fun createCourseForComplexProgressTest(): Long {
        val courseAddRequest = CourseAddRequest(1, 1, "test", "test", "test", "short", null, "url", null, "url", CourseStatus.PUBLIC, 1)
        val courseId = restWithAdminRole().postForEntity<CourseDto>("/course", courseAddRequest).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!.id
        }

        val chapterAddRequest = ChapterAddRequest(courseId, "test", 1)
        val chapterIdFirst = restWithAdminRole().postForEntity<ChapterDto>("/chapter", chapterAddRequest).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!.id
        }

        val chapterAddRequest2 = ChapterAddRequest(courseId, "test", 2)
        val chapterIdSecond = restWithAdminRole().postForEntity<ChapterDto>("/chapter", chapterAddRequest2).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!.id
        }

        val lecture1ForChapter1 = LectureAddRequest(chapterIdFirst, "test", 1, "content")
        val lecture2ForChapter1 = LectureAddRequest(chapterIdFirst, "test", 2, "content")
        val lecture3ForChapter1 = LectureAddRequest(chapterIdFirst, "test", 3, "content")
        restWithAdminRole().postForEntity<LectureDto>("/lecture", lecture1ForChapter1)
        restWithAdminRole().postForEntity<LectureDto>("/lecture", lecture2ForChapter1)
        restWithAdminRole().postForEntity<LectureDto>("/lecture", lecture3ForChapter1)

        val lecture1ForChapter2 = LectureAddRequest(chapterIdSecond, "test", 1, "content")
        val lecture2ForChapter2 = LectureAddRequest(chapterIdSecond, "test", 2, "content")
        restWithAdminRole().postForEntity<LectureDto>("/lecture", lecture1ForChapter2)
        restWithAdminRole().postForEntity<LectureDto>("/lecture", lecture2ForChapter2)

        return courseId
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
        return restWithAdminRole().postForEntity<BadRequestException>(
            "/progress/reset",
            body
        ).statusCode shouldBe HttpStatus.BAD_REQUEST
    }
}
