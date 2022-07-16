package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.dto.AuthorOverviewDto
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.api.request.AuthorEditRequest
import sk.streetofcode.courseplatformbackend.configuration.SpringBootTestAnnotation
import sk.streetofcode.courseplatformbackend.model.Author

@SpringBootTestAnnotation
class AuthorIntegrationTests : IntegrationTests() {
    init {
        "get authors" {
            val authorsResponse = getAuthors()
            authorsResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = authorsResponse.headers["Content-Range"]
            contentRange shouldBe listOf("author 0-2/2")

            val authors = authorsResponse.body!!
            authors.size shouldBe 2
        }

        "get author ids" {
            val authorsResponse = getAuthorIds()
            authorsResponse.statusCode shouldBe HttpStatus.OK

            val authorIds = authorsResponse.body!!
            authorIds shouldBe listOf(1, 2)
        }

        "add author" {
            val author = addAuthor(AuthorAddRequest("testName", "testUrl", "title", "email", "testDescription"))

            val fetchedAuthor = getAuthor(author.id!!)
            fetchedAuthor.name shouldBe "testName"
            fetchedAuthor.imageUrl shouldBe "testUrl"
            fetchedAuthor.description shouldBe "testDescription"
            fetchedAuthor.coursesTitle shouldBe "title"
            fetchedAuthor.email shouldBe "email"
        }

        "edit author" {
            editAuthorNotFound(999, AuthorEditRequest(1, "", "", "", "", ""))

            val author = addAuthor(AuthorAddRequest("testName", "testUrl", "title", "email", "testDescription"))

            val editedAuthor = editAuthor(
                author.id!!,
                AuthorEditRequest(
                    author.id!!,
                    "editedTestName",
                    "editedTestUrl",
                    "editedTitle",
                    "editedEmail",
                    "editedTestDescription"
                )
            )

            val fetchedAuthor = getAuthor(editedAuthor.id!!)
            fetchedAuthor.id shouldBe author.id
            fetchedAuthor.courses shouldBe author.courses
            fetchedAuthor.name shouldBe "editedTestName"
            fetchedAuthor.imageUrl shouldBe "editedTestUrl"
            fetchedAuthor.coursesTitle shouldBe "editedTitle"
            fetchedAuthor.email shouldBe "editedEmail"
            fetchedAuthor.description shouldBe "editedTestDescription"
        }

        "delete author" {
            val author = addAuthor(AuthorAddRequest("testName", "testUrl", "title", "email", "testDescription"))

            val removedAuthor = deleteAuthor(author.id!!)
            removedAuthor.shouldBe(author)

            getAuthorNotFound(author.id!!)
        }

        "get author overview" {
            val author = addAuthor(AuthorAddRequest("testName", "testUrl", "title", "email", "testDescription"))
            val authorOverview = getAuthorOverview(author.id!!)

            authorOverview.id shouldBe author.id
            authorOverview.name shouldBe "testName"
            authorOverview.description shouldBe "testDescription"
            authorOverview.imageUrl shouldBe "testUrl"
            authorOverview.description shouldBe "testDescription"
            authorOverview.coursesTitle shouldBe "title"
            authorOverview.courses.size shouldBe 0
        }

        "fail get author overview, not found" {
            getAuthorOverviewNotFound(99)
        }
    }

    private fun getAuthors(): ResponseEntity<List<Author>> {
        return restWithAdminRole().getForEntity<List<Author>>("/author")
    }

    private fun getAuthorIds(): ResponseEntity<List<Long>> {
        return restWithAdminRole().getForEntity<List<Long>>("/author/id")
    }

    private fun getAuthor(authorId: Long): Author {
        return restWithAdminRole().getForEntity<Author>("/author/$authorId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getAuthorNotFound(authorId: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/author/$authorId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editAuthorNotFound(authorId: Long, body: AuthorEditRequest) {
        return restWithAdminRole().putForEntity<ResourceNotFoundException>("/author/$authorId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun addAuthor(body: AuthorAddRequest): Author {
        return restWithAdminRole().postForEntity<Author>("/author", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editAuthor(authorId: Long, body: AuthorEditRequest): Author {
        return restWithAdminRole().putForEntity<Author>("/author/$authorId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteAuthor(authorId: Long): Author {
        return restWithAdminRole().deleteForEntity<Author>("/author/$authorId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getAuthorOverview(authorId: Long): AuthorOverviewDto {
        return restWithAdminRole().getForEntity<AuthorOverviewDto>("/author/$authorId/overview").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getAuthorOverviewNotFound(authorId: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/author/$authorId/overview").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }
}
