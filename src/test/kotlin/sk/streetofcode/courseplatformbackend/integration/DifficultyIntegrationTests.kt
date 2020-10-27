package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RequestDocumentClarificationBackendTests : IntegrationTests() {
    init {
        "Can add difficulty" {
            val difficulty = addDifficulty(DifficultyAddRequest("testName", "testDescription"))

            val fetchedDifficulty = getDifficulty(difficulty.id!!)
            fetchedDifficulty.name shouldBe "testName"
            fetchedDifficulty.description shouldBe "testDescription"
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

            val removedDifficulty = deleteDifficulty(difficulty.id!!)
            removedDifficulty.shouldBe(difficulty)

            getDifficultyNotFound(difficulty.id!!)
        }
    }
}
