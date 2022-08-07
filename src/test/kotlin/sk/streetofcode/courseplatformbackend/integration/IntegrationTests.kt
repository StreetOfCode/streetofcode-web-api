package sk.streetofcode.courseplatformbackend.integration

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.request.SocUserAddRequest
import sk.streetofcode.courseplatformbackend.client.youtube.YoutubeApiClient
import sk.streetofcode.courseplatformbackend.model.SocUser
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

open class IntegrationTests : StringSpec() {
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    protected var userId: String = "moNoTwZcU5Nwg4qMBBVW9uJBQM12"

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
            Mockito.`when`(authenticationService.isAuthenticated()).thenCallRealMethod()
            setUserId("moNoTwZcU5Nwg4qMBBVW9uJBQM12")
        }
    }

    @JvmName("setUserId_custom")
    protected fun setUserId(userId: String) {
        this.userId = userId
        Mockito.`when`(authenticationService.getUserId()).thenReturn(userId)
    }

    protected fun createRandomUser() {
        val randomUserId = getRandomUserId()
        setUserId(randomUserId)
        createUser(randomUserId)
    }

    protected fun getRandomString(): String {
        return getRandomUserId()
    }

    private fun getRandomUserId(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..12)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun createUser(userId: String): String {
        val socUser = SocUser(
            userId,
            "John Bool",
            "john.bool.bool@gmail.com",
            null,
            false
        )
        val socUserAddRequest = SocUserAddRequest(socUser.firebaseId, socUser.name, socUser.email, socUser.imageUrl, false, false)
        return restWithUserRole().postForEntity<SocUser>("/user", socUserAddRequest).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.firebaseId
        }
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
