package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.DifficultyAddRequest
import sk.streetofcode.webapi.api.request.DifficultyEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation
import sk.streetofcode.webapi.model.Difficulty

@SpringBootTestAnnotation
class DifficultyIntegrationTests : IntegrationTests() {
    init {
        "get difficulties" {
            val difficultiesResponse = getDifficulties()
            difficultiesResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = difficultiesResponse.headers["Content-Range"]
            contentRange shouldBe listOf("difficulty 0-3/3")

            val difficulties = difficultiesResponse.body!!
            difficulties.size shouldBe 3
        }

        "add difficulty" {
            val difficulty = addDifficulty(DifficultyAddRequest("testName", 1))

            val fetchedDifficulty = getDifficulty(difficulty.id!!)
            fetchedDifficulty.name shouldBe "testName"
            fetchedDifficulty.skillLevel shouldBe 1
        }

        "edit difficulty" {
            editDifficultyNotFound(999, DifficultyEditRequest(1, "", 3))

            val difficulty = addDifficulty(DifficultyAddRequest("testName", 3))

            val editedDifficulty = editDifficulty(
                difficulty.id!!,
                DifficultyEditRequest(difficulty.id!!, "editedTestName", 4)
            )

            val fetchedDifficulty = getDifficulty(editedDifficulty.id!!)
            fetchedDifficulty.id shouldBe difficulty.id
            fetchedDifficulty.courses shouldBe difficulty.courses
            fetchedDifficulty.name shouldBe "editedTestName"
            fetchedDifficulty.skillLevel shouldBe 4
        }

        "delete difficulty" {
            val difficulty = addDifficulty(DifficultyAddRequest("testName", 2))

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
