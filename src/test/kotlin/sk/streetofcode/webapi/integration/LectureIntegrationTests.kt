package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.mockito.Mockito
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.webapi.api.dto.LectureDto
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.LectureAddRequest
import sk.streetofcode.webapi.api.request.LectureEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class LectureIntegrationTests : IntegrationTests() {
    init {
        "add lecture" {
            val videoUrl = "https://www.youtube.com/embed/z1At9Jk4sqE"
            val videoDuration = 100
            Mockito.`when`(vimeoApiClient.getVideoDurationInSeconds(videoUrl)).thenReturn(videoDuration)

            val lecture = addLecture(LectureAddRequest(1, "testName", 1500, "testContent", videoUrl, false))

            val fetchedLecture = getLecture(lecture.id)
            fetchedLecture.name shouldBe "testName"
            fetchedLecture.chapter.id shouldBe 1
            fetchedLecture.lectureOrder shouldBe 1500
            fetchedLecture.content shouldBe "testContent"
            fetchedLecture.videoUrl shouldBe videoUrl
            fetchedLecture.videoDurationSeconds shouldBe videoDuration
            fetchedLecture.allowPreviewWhenPaid shouldBe false

            // get lectures
            val lecturesResponse = getLectures()
            lecturesResponse.find { it.id == lecture.id } shouldBe lecture
        }

        "add lecture invalid videoUrl" {
            val videoUrl = "invalidUrl"
            Mockito.`when`(vimeoApiClient.getVideoDurationInSeconds(videoUrl)).thenThrow(InternalErrorException(""))

            addLectureInvalidVideoUrl(LectureAddRequest(1, "testName", 1, "testContent", videoUrl, false))
        }

        "edit lecture" {
            editLectureNotFound(999, LectureEditRequest(1, "testNameEdited", 1, false, "testContentEdited"))

            val lecture = addLecture(LectureAddRequest(1, "testName", 1, allowPreviewWhenPaid = true, content = "testContent"))
            lecture.videoUrl shouldBe null
            lecture.videoDurationSeconds shouldBe 0
            lecture.allowPreviewWhenPaid shouldBe true

            val videoUrl = "https://www.youtube.com/embed/z1At9Jk4sqE"
            val videoDuration = 100
            Mockito.`when`(vimeoApiClient.getVideoDurationInSeconds(videoUrl)).thenReturn(videoDuration)

            val editedLecture = editLecture(
                lecture.id, LectureEditRequest(lecture.id, "testNameEdited", 1, false, content = "testContentEdited", videoUrl)
            )

            val fetchedLecture = getLecture(editedLecture.id)
            fetchedLecture.name shouldBe "testNameEdited"
            fetchedLecture.chapter.id shouldBe 1
            fetchedLecture.lectureOrder shouldBe 1
            fetchedLecture.content shouldBe "testContentEdited"
            fetchedLecture.videoUrl shouldBe videoUrl
            fetchedLecture.videoDurationSeconds shouldBe videoDuration
            fetchedLecture.allowPreviewWhenPaid shouldBe false
        }

        "delete lecture" {
            val lecture = addLecture(LectureAddRequest(1, "testName", 1, allowPreviewWhenPaid = false, content = "testContent"))

            val removedLecture = deleteLecture(lecture.id)
            removedLecture shouldBe lecture

            getLectureNotFound(lecture.id)
        }
    }

    private fun getLectures(): List<LectureDto> {
        return restWithAdminRole().getForEntity<Array<LectureDto>>("/lecture").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    private fun getLecture(lectureId: Long): LectureDto {
        return restWithAdminRole().getForEntity<LectureDto>("/lecture/$lectureId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getLectureNotFound(lectureId: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/lecture/$lectureId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editLectureNotFound(lectureId: Long, body: LectureEditRequest) {
        return restWithAdminRole().putForEntity<ResourceNotFoundException>("/lecture/$lectureId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun addLecture(body: LectureAddRequest): LectureDto {
        return restWithAdminRole().postForEntity<LectureDto>("/lecture", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun addLectureInvalidVideoUrl(body: LectureAddRequest) {
        return restWithAdminRole().postForEntity<InternalErrorException>("/lecture", body).let {
            it.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    private fun editLecture(lectureId: Long, body: LectureEditRequest): LectureDto {
        return restWithAdminRole().putForEntity<LectureDto>("/lecture/$lectureId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteLecture(lectureId: Long): LectureDto {
        return restWithAdminRole().deleteForEntity<LectureDto>("/lecture/$lectureId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
