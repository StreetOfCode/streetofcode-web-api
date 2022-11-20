package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.webapi.api.dto.ChapterDto
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.ChapterAddRequest
import sk.streetofcode.webapi.api.request.ChapterEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class ChapterIntegrationTests : IntegrationTests() {
    init {
        "add chapter" {
            val chapter = addChapter(ChapterAddRequest(1, "testName", 1))

            val fetchedChapter = getChapter(chapter.id)
            fetchedChapter.name shouldBe "testName"
            fetchedChapter.course.id shouldBe 1
            fetchedChapter.chapterOrder shouldBe 1

            // get chapters
            val chaptersResponse = getChapters()
            chaptersResponse.find { it.id == chapter.id } shouldBe chapter
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

    private fun getChapters(): List<ChapterDto> {
        return restWithAdminRole().getForEntity<Array<ChapterDto>>("/chapter").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    private fun getChapter(chapterId: Long): ChapterDto {
        return restWithAdminRole().getForEntity<ChapterDto>("/chapter/$chapterId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getChapterNotFound(chapterId: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/chapter/$chapterId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editChapterNotFound(chapterId: Long, body: ChapterEditRequest) {
        return restWithAdminRole().putForEntity<ResourceNotFoundException>("/chapter/$chapterId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun addChapter(body: ChapterAddRequest): ChapterDto {
        return restWithAdminRole().postForEntity<ChapterDto>("/chapter", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editChapter(chapterId: Long, body: ChapterEditRequest): ChapterDto {
        return restWithAdminRole().putForEntity<ChapterDto>("/chapter/$chapterId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteChapter(chapterId: Long): ChapterDto {
        return restWithAdminRole().deleteForEntity<ChapterDto>("/chapter/$chapterId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
