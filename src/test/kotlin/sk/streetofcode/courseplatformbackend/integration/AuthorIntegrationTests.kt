package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

        "add author" {
            val author = addAuthor(AuthorAddRequest("testName", "testUrl", "testDescription"))

            val fetchedAuthor = getAuthor(author.id!!)
            fetchedAuthor.name shouldBe "testName"
            fetchedAuthor.url shouldBe "testUrl"
            fetchedAuthor.description shouldBe "testDescription"
        }

        "edit author" {
            editAuthorNotFound(999, AuthorEditRequest(1, "", "", ""))

            val author = addAuthor(AuthorAddRequest("testName", "testUrl", "testDescription"))

            val editedAuthor = editAuthor(
                author.id!!,
                AuthorEditRequest(author.id!!, "editedTestName", "editedTestUrl", "editedTestDescription")
            )

            val fetchedAuthor = getAuthor(editedAuthor.id!!)
            fetchedAuthor.id shouldBe author.id
            fetchedAuthor.courses shouldBe author.courses
            fetchedAuthor.name shouldBe "editedTestName"
            fetchedAuthor.url shouldBe "editedTestUrl"
            fetchedAuthor.description shouldBe "editedTestDescription"
        }

        "delete author" {
            val author = addAuthor(AuthorAddRequest("testName", "testUrl", "testDescription"))

            val removedAuthor = deleteAuthor(author.id!!)
            removedAuthor.shouldBe(author)

            getAuthorNotFound(author.id!!)
        }
    }

    private fun getAuthors(): ResponseEntity<List<Author>> {
        return restWithAdminRole().getForEntity<List<Author>>("/author")
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
}
