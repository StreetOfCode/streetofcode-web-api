package sk.streetofcode.courseplatformbackend.integration

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.spring.SpringListener
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.client.youtube.YoutubeApiClient
import sk.streetofcode.courseplatformbackend.service.AuthenticationService
import java.util.UUID

open class IntegrationTests : StringSpec() {
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    protected var userId: UUID = UUID.fromString("bb9e0186-aaae-11eb-bcbc-0242ac130002")

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @MockBean
    protected lateinit var youtubeApiClient: YoutubeApiClient

    @MockBean
    protected lateinit var authenticationService: AuthenticationService

    init {
        beforeTest {
            Mockito.`when`(authenticationService.isAdmin()).thenCallRealMethod()
            Mockito.`when`(authenticationService.isUser()).thenCallRealMethod()
            setUserId(UUID.fromString("bb9e0186-aaae-11eb-bcbc-0242ac130002"))
        }
    }

    @JvmName("setUserId_custom")
    protected fun setUserId(userId: UUID) {
        this.userId = userId
        Mockito.`when`(authenticationService.getUserId()).thenReturn(userId)
    }

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
