package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.dto.ChapterDto
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.api.request.ChapterEditRequest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChapterIntegrationTests : IntegrationTests() {
    init {
        "get chapters" {
            val chaptersResponse = getChapters()
            chaptersResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = chaptersResponse.headers["Content-Range"]
            contentRange shouldBe listOf("chapter 0-4/4")

            val chapters = chaptersResponse.body!!
            chapters.size shouldBe 4
        }

        "add chapter" {
            val chapter = addChapter(ChapterAddRequest(1, "testName", 1))

            val fetchedChapter = getChapter(chapter.id)
            fetchedChapter.name shouldBe "testName"
            fetchedChapter.course.id shouldBe 1
            fetchedChapter.chapterOrder shouldBe 1
        }

        "edit chapter" {
            editChapterNotFound(999, ChapterEditRequest(1, "testNameEdited", 1))

            val chapter = addChapter(ChapterAddRequest(1, "testName", 1))

            val editedChapter = editChapter(
                    chapter.id, ChapterEditRequest(chapter.id, "testNameEdited", 1)
            )

            val fetchedChapter = getChapter(editedChapter.id)
            fetchedChapter.name shouldBe "testNameEdited"
            fetchedChapter.course.id shouldBe 1
            fetchedChapter.chapterOrder shouldBe 1
        }

        "delete chapter" {
            val chapter = addChapter(ChapterAddRequest(1, "testName", 1))

            val removedChapter = deleteChapter(chapter.id)
            removedChapter.shouldBe(chapter)

            getChapterNotFound(chapter.id)
        }
    }


    private fun getChapters(): ResponseEntity<List<ChapterDto>> {
        return restTemplate.getForEntity("/chapter")
    }

    private fun getChapter(chapterId: Long): ChapterDto {
        return restTemplate.getForEntity<ChapterDto>("/chapter/$chapterId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getChapterNotFound(chapterId: Long) {
        return restTemplate.getForEntity<ResourceNotFoundException>("/chapter/$chapterId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editChapterNotFound(chapterId: Long, body: ChapterEditRequest) {
        return restTemplate.putForEntity<ResourceNotFoundException>("/chapter/$chapterId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun addChapter(body: ChapterAddRequest): ChapterDto {
        return restTemplate.postForEntity<ChapterDto>("/chapter", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editChapter(chapterId: Long, body: ChapterEditRequest): ChapterDto {
        return restTemplate.putForEntity<ChapterDto>("/chapter/$chapterId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteChapter(chapterId: Long): ChapterDto {
        return restTemplate.deleteForEntity<ChapterDto>("/chapter/$chapterId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
