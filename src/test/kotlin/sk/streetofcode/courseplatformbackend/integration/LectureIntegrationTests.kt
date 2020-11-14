package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureEditRequest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LectureIntegrationTests : IntegrationTests() {
    init {
        "get lectures" {
            val lecturesResponse = getLectures()
            lecturesResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = lecturesResponse.headers["Content-Range"]
            contentRange shouldBe listOf("lecture 0-9/9")

            val lectures = lecturesResponse.body!!
            lectures.size shouldBe 9
        }

        "add lecture" {
            val lecture = addLecture(LectureAddRequest(1, "testName", 1, "testContent"))

            val fetchedLecture = getLecture(lecture.id)
            fetchedLecture.name shouldBe "testName"
            fetchedLecture.chapter.id shouldBe 1
            fetchedLecture.lectureOrder shouldBe 1
            fetchedLecture.content shouldBe "testContent"
        }

        "edit lecture" {
            editLectureNotFound(999, LectureEditRequest(1, "testNameEdited", 1, "testContentEdited"))

            val lecture = addLecture(LectureAddRequest(1, "testName", 1, "testContent"))

            val editedLecture = editLecture(
                    lecture.id, LectureEditRequest(lecture.id, "testNameEdited", 1, "testContentEdited")
            )

            val fetchedLecture = getLecture(editedLecture.id)
            fetchedLecture.name shouldBe "testNameEdited"
            fetchedLecture.chapter.id shouldBe 1
            fetchedLecture.lectureOrder shouldBe 1
            fetchedLecture.content shouldBe "testContentEdited"
        }

        "delete lecture" {
            val lecture = addLecture(LectureAddRequest(1, "testName", 1, "testContent"))

            val removedLecture = deleteLecture(lecture.id)
            removedLecture.shouldBe(lecture)

            getLectureNotFound(lecture.id)
        }
    }


    private fun getLectures(): ResponseEntity<List<LectureDto>> {
        return restTemplate.getForEntity("/lecture")
    }

    private fun getLecture(lectureId: Long): LectureDto {
        return restTemplate.getForEntity<LectureDto>("/lecture/$lectureId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getLectureNotFound(lectureId: Long) {
        return restTemplate.getForEntity<ResourceNotFoundException>("/lecture/$lectureId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editLectureNotFound(lectureId: Long, body: LectureEditRequest) {
        return restTemplate.putForEntity<ResourceNotFoundException>("/lecture/$lectureId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun addLecture(body: LectureAddRequest): LectureDto {
        return restTemplate.postForEntity<LectureDto>("/lecture", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editLecture(lectureId: Long, body: LectureEditRequest): LectureDto {
        return restTemplate.putForEntity<LectureDto>("/lecture/$lectureId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteLecture(lectureId: Long): LectureDto {
        return restTemplate.deleteForEntity<LectureDto>("/lecture/$lectureId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
