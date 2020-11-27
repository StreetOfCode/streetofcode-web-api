package sk.streetofcode.courseplatformbackend.integration

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

open class IntegrationTests : StringSpec() {
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    // helper method to do the same thing as postForEntity but with PUT method
    protected inline fun <reified T> TestRestTemplate.putForEntity(url: String, body: Any): ResponseEntity<T> {
        return exchange(url, HttpMethod.PUT, HttpEntity(body), T::class.java)
    }

    protected inline fun <reified T> TestRestTemplate.deleteForEntity(url: String): ResponseEntity<T> {
        return exchange(url, HttpMethod.DELETE, null, T::class.java)
    }

    protected fun restWithAdminRole(): TestRestTemplate {
        return restTemplate.withBasicAuth("admin", "admin")
    }

    protected fun restWithUserRole(): TestRestTemplate {
        return restTemplate.withBasicAuth("user", "user")
    }

}
