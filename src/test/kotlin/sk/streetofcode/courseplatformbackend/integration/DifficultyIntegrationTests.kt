package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest
import sk.streetofcode.courseplatformbackend.configuration.SpringBootTestAnnotation
import sk.streetofcode.courseplatformbackend.model.Difficulty

@SpringBootTestAnnotation
class DifficultyIntegrationTests : IntegrationTests() {
    init {
        "get difficulties" {
            val difficultiesResponse = getDifficulties()
            difficultiesResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = difficultiesResponse.headers["Content-Range"]
            contentRange shouldBe listOf("difficulty 0-2/2")

            val difficulties = difficultiesResponse.body!!
            difficulties.size shouldBe 2
        }

        "add difficulty" {
            val difficulty = addDifficulty(DifficultyAddRequest("testName", "testDescription"))

            val fetchedDifficulty = getDifficulty(difficulty.id!!)
            fetchedDifficulty.name shouldBe "testName"
            fetchedDifficulty.description shouldBe "testDescription"
        }

        "edit difficulty" {
            editDifficultyNotFound(999, DifficultyEditRequest(1, "", ""))

            val difficulty = addDifficulty(DifficultyAddRequest("testName", "testDescription"))

            val editedDifficulty = editDifficulty(
                difficulty.id!!,
                DifficultyEditRequest(difficulty.id!!, "editedTestName", "editedTestDescription")
            )

            val fetchedDifficulty = getDifficulty(editedDifficulty.id!!)
            fetchedDifficulty.id shouldBe difficulty.id
            fetchedDifficulty.courses shouldBe difficulty.courses
            fetchedDifficulty.name shouldBe "editedTestName"
            fetchedDifficulty.description shouldBe "editedTestDescription"
        }

        "delete difficulty" {
            val difficulty = addDifficulty(DifficultyAddRequest("testName", "testDescription"))

            val removedDifficulty = deleteDifficulty(difficulty.id!!)
            removedDifficulty.shouldBe(difficulty)

            getDifficultyNotFound(difficulty.id!!)
        }
    }

    private fun getDifficulties(): ResponseEntity<List<Difficulty>> {
        return restWithAdminRole().getForEntity<List<Difficulty>>("/difficulty")
    }

    private fun getDifficulty(difficultyId: Long): Difficulty {
        return restWithAdminRole().getForEntity<Difficulty>("/difficulty/$difficultyId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getDifficultyNotFound(difficultyId: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/difficulty/$difficultyId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editDifficultyNotFound(difficultyId: Long, body: DifficultyEditRequest) {
        return restWithAdminRole().putForEntity<ResourceNotFoundException>("/difficulty/$difficultyId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun addDifficulty(body: DifficultyAddRequest): Difficulty {
        return restWithAdminRole().postForEntity<Difficulty>("/difficulty", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editDifficulty(difficultyId: Long, body: DifficultyEditRequest): Difficulty {
        return restWithAdminRole().putForEntity<Difficulty>("/difficulty/$difficultyId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteDifficulty(difficultyId: Long): Difficulty {
        return restWithAdminRole().deleteForEntity<Difficulty>("/difficulty/$difficultyId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
