package sk.streetofcode.webapi.integration

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.webapi.api.dto.IsOwnedByUserDto
import sk.streetofcode.webapi.api.request.SocUserAddRequest
import sk.streetofcode.webapi.client.stripe.StripeApiClient
import sk.streetofcode.webapi.client.vimeo.VimeoApiClient
import sk.streetofcode.webapi.db.repository.NewsletterRegistrationRepository
import sk.streetofcode.webapi.model.SocUser
import sk.streetofcode.webapi.service.AuthenticationService
import sk.streetofcode.webapi.service.CourseProductServiceImpl

open class IntegrationTests : StringSpec() {
    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    protected var userId: String = "moNoTwZcU5Nwg4qMBBVW9uJBQM12"

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var newsletterRegistrationRepository: NewsletterRegistrationRepository

    @MockBean
    protected lateinit var vimeoApiClient: VimeoApiClient

    @MockBean
    protected lateinit var stripeApiClient: StripeApiClient

    @MockBean
    protected lateinit var authenticationService: AuthenticationService

    @MockBean
    protected lateinit var courseProductService: CourseProductServiceImpl

    init {
        beforeTest {
            Mockito.`when`(authenticationService.isAdmin()).thenCallRealMethod()
            Mockito.`when`(authenticationService.isUser()).thenCallRealMethod()
            Mockito.`when`(authenticationService.isAuthenticated()).thenCallRealMethod()

            Mockito.`when`(courseProductService.isOwnedByUser(anyLong())).thenReturn(IsOwnedByUserDto(true))

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
        val socUserAddRequest = SocUserAddRequest(socUser.firebaseId, socUser.name, socUser.email, socUser.imageUrl, false, false, "")
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
