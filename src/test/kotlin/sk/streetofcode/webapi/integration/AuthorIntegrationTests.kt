package sk.streetofcode.webapi.integration

import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.webapi.api.dto.AuthorOverviewDto
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.AuthorAddRequest
import sk.streetofcode.webapi.api.request.AuthorEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation
import sk.streetofcode.webapi.model.Author

@SpringBootTestAnnotation
class AuthorIntegrationTests : IntegrationTests() {
    init {
        "add author" {
            val uniqueSlug = getRandomString()
            val author = addAuthor(AuthorAddRequest("testName", uniqueSlug, "testUrl", "title", "email", "testDescription"))

            val fetchedAuthor = getAuthor(author.id!!)
            fetchedAuthor.name shouldBe "testName"
            fetchedAuthor.slug shouldBe uniqueSlug
            fetchedAuthor.imageUrl shouldBe "testUrl"
            fetchedAuthor.description shouldBe "testDescription"
            fetchedAuthor.coursesTitle shouldBe "title"
            fetchedAuthor.email shouldBe "email"

            // get authors
            val authorsResponse = getAuthors()
            authorsResponse.size shouldBeGreaterThan 0
            authorsResponse.find { it.id == author.id } shouldBe author

            // get author slugs
            val authorsSlugsResponse = getAuthorSlugs()
            authorsSlugsResponse.statusCode shouldBe HttpStatus.OK

            val authorSlugs = authorsSlugsResponse.body!!
            authorSlugs.find { it == uniqueSlug } shouldBe uniqueSlug
        }

        "edit author" {
            editAuthorNotFound(999, AuthorEditRequest(1, "", "", "", "", "", ""))

            val uniqueSlug = getRandomString()
            val author = addAuthor(AuthorAddRequest("testName", uniqueSlug, "testUrl", "title", "email", "testDescription"))

            val editedAuthor = editAuthor(
                author.id!!,
                AuthorEditRequest(
                    author.id!!,
                    "editedTestName",
                    "editedSlug",
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
            fetchedAuthor.slug shouldBe "editedSlug"
            fetchedAuthor.imageUrl shouldBe "editedTestUrl"
            fetchedAuthor.coursesTitle shouldBe "editedTitle"
            fetchedAuthor.email shouldBe "editedEmail"
            fetchedAuthor.description shouldBe "editedTestDescription"
        }

        "delete author" {
            val uniqueSlug = getRandomString()
            val author = addAuthor(AuthorAddRequest("testName", uniqueSlug, "testUrl", "title", "email", "testDescription"))

            val removedAuthor = deleteAuthor(author.id!!)
            removedAuthor.shouldBe(author)

            getAuthorNotFound(author.id!!)
        }

        "get author overview" {
            val uniqueSlug = getRandomString()
            val author = addAuthor(AuthorAddRequest("testName", uniqueSlug, "testUrl", "title", "email", "testDescription"))
            val authorOverview = getAuthorOverview(author.slug)

            authorOverview.id shouldBe author.id
            authorOverview.name shouldBe "testName"
            authorOverview.slug shouldBe uniqueSlug
            authorOverview.description shouldBe "testDescription"
            authorOverview.imageUrl shouldBe "testUrl"
            authorOverview.description shouldBe "testDescription"
            authorOverview.coursesTitle shouldBe "title"
            authorOverview.courses.size shouldBe 0
        }

        "fail get author overview, not found" {
            getAuthorOverviewNotFound("bla-bla-bla-slug")
        }
    }

    private fun getAuthors(): List<Author> {
        class ListOfAuthors : ParameterizedTypeReference<List<Author>>()
        return restWithAdminRole().exchange("/author", HttpMethod.GET, null, ListOfAuthors()).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getAuthorSlugs(): ResponseEntity<List<String>> {
        return restWithAdminRole().getForEntity("/author/slug")
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

    private fun getAuthorOverview(slug: String): AuthorOverviewDto {
        return restWithAdminRole().getForEntity<AuthorOverviewDto>("/author/$slug/overview").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getAuthorOverviewNotFound(slug: String) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/author/$slug/overview").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }
}
