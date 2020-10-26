package sk.streetofcode.courseplatformbackend.integration

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest
import sk.streetofcode.courseplatformbackend.model.Difficulty

open class IntegrationTests : StringSpec() {
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    // helper method to do the same thing as postForEntity but with PUT method
    private inline fun <reified T> TestRestTemplate.putForEntity(url: String, body: Any): ResponseEntity<T> {
        return exchange(url, HttpMethod.PUT, HttpEntity(body), T::class.java)
    }

    private inline fun <reified T> TestRestTemplate.deleteForEntity(url: String): ResponseEntity<T> {
        return exchange(url, HttpMethod.DELETE, null, T::class.java)
    }

    protected fun getDifficulties(): List<Difficulty> {
        return restTemplate.getForEntity<List<Difficulty>>("/difficulty").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    protected fun getDifficulty(difficultyId: Long): Difficulty {
        return restTemplate.getForEntity<Difficulty>("/difficulty/$difficultyId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    protected fun addDifficulty(body: DifficultyAddRequest): Difficulty {
        return restTemplate.postForEntity<Difficulty>("/difficulty", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    protected fun editDifficulty(difficultyId: Long, body: DifficultyEditRequest): Difficulty {
        return restTemplate.putForEntity<Difficulty>("/difficulty/$difficultyId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    protected fun deleteDifficulty(difficultyId: Long): Difficulty {
        return restTemplate.deleteForEntity<Difficulty>("/difficulty/$difficultyId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
