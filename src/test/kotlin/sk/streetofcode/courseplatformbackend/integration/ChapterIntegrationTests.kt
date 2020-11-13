package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.api.request.ChapterEditRequest
import sk.streetofcode.courseplatformbackend.model.Chapter

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

            val fetchedChapter = getChapter(chapter.id!!)
            fetchedChapter.name shouldBe "testName"
            fetchedChapter.course.id shouldBe 1
            fetchedChapter.chapterOrder shouldBe 1
        }

        "edit chapter" {
            editChapterNotFound(999, ChapterEditRequest(1, "testNameEdited", 1))

            val chapter = addChapter(ChapterAddRequest(1, "testName", 1))

            val editedChapter = editChapter(
                    chapter.id!!, ChapterEditRequest(1, "testNameEdited", 1)
            )

            val fetchedChapter = getChapter(editedChapter.id!!)
            fetchedChapter.name shouldBe "testNameEdited"
            fetchedChapter.course.id shouldBe 1
            fetchedChapter.chapterOrder shouldBe 1
        }

        "delete chapter" {
            val chapter = addChapter(ChapterAddRequest(1, "testName", 1))

            val removedChapter = deleteChapter(chapter.id!!)
            removedChapter.shouldBe(chapter)

            getChapterNotFound(chapter.id!!)
        }
    }


    private fun getChapters(): ResponseEntity<List<Chapter>> {
        return restTemplate.getForEntity<List<Chapter>>("/chapter")
    }

    private fun getChapter(chapterId: Long): Chapter {
        return restTemplate.getForEntity<Chapter>("/chapter/$chapterId").let {
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

    private fun addChapter(body: ChapterAddRequest): Chapter {
        return restTemplate.postForEntity<Chapter>("/chapter", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editChapter(chapterId: Long, body: ChapterEditRequest): Chapter {
        return restTemplate.putForEntity<Chapter>("/chapter/$chapterId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteChapter(chapterId: Long): Chapter {
        return restTemplate.deleteForEntity<Chapter>("/chapter/$chapterId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
