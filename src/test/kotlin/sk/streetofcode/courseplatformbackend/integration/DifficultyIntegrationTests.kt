package sk.streetofcode.courseplatformbackend.integration

import io.kotest.assertions.shouldFail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest
import sk.streetofcode.courseplatformbackend.model.Difficulty
import sk.streetofcode.courseplatformbackend.rest.TestClass
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RequestDocumentClarificationBackendTests : IntegrationTests() {
    init {
        "Can add difficulty" {
            // val difficulty = addDifficulty(DifficultyAddRequest("testName", "testDescription"))
            //
            // val fetchedDifficulty = getDifficulty(difficulty.id!!)
            // fetchedDifficulty.name shouldBe "testName"
            // fetchedDifficulty.description shouldBe "testDescription"
            restTemplate.getForEntity<TestClass>("/difficulty", null).let {
                it.statusCode shouldBe HttpStatus.OK
                it.body!!
            }
        }

        "Can edit difficulty" {
            val difficulty = addDifficulty(DifficultyAddRequest("testName", "testDescription"))

            val editedDifficulty = editDifficulty(
                difficulty.id!!,
                DifficultyEditRequest(difficulty.id!!, "editedTestName", "editedTestDescription")
            )

            val fetchedDifficulty = getDifficulty(editedDifficulty.id!!)
            fetchedDifficulty.name shouldBe "editedTestName"
            fetchedDifficulty.description shouldBe "editedTestDescription"
        }

        "Can delete difficulty" {
            val difficulty = addDifficulty(DifficultyAddRequest("testName", "testDescription"))

            deleteDifficulty(difficulty.id!!)

            shouldThrow<ResourceNotFoundException> { getDifficulty(difficulty.id!!) }
        }
    }
}
