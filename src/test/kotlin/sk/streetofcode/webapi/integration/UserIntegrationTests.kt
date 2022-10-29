package sk.streetofcode.webapi.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.SocUserAddRequest
import sk.streetofcode.webapi.api.request.SocUserEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation
import sk.streetofcode.webapi.model.SocUser

@SpringBootTestAnnotation
class UserIntegrationTests : IntegrationTests() {
    init {
        "get user" {
            val fetchedUser = getUser()
            fetchedUser.firebaseId shouldBe "moNoTwZcU5Nwg4qMBBVW9uJBQM12"
            fetchedUser.name shouldBe "Gabriel Kerekeš"
            fetchedUser.email shouldBe "gabriel@streetofcode.sk"
            fetchedUser.imageUrl shouldBe "https://wp.streetofcode.sk/wp-content/uploads/2020/04/7520735.png"

            setUserId("Dk71hPkR9Fc6SJma3S1NvGcrkHe2")
            val anotherFetchedUser = getUser()
            anotherFetchedUser.firebaseId shouldBe "Dk71hPkR9Fc6SJma3S1NvGcrkHe2"
            anotherFetchedUser.name shouldBe "Jakub Jahič"
            anotherFetchedUser.email shouldBe "jakub@streetofcode.sk"
            anotherFetchedUser.imageUrl shouldBe "https://wp.streetofcode.sk/wp-content/uploads/2019/04/JFinal-768x576.jpg"

            setUserId("non-existing-user-id")
            failGettingUser()
        }

        "post user" {
            val newUserId = "QgXv1QVvoYZF6W46pwH51PzpJx73"
            setUserId(newUserId)
            val expectedSocUser = SocUser(
                newUserId,
                "John Bool",
                "john.bool.bool@gmail.com",
                "wtf",
                false
            )
            val user = postUser(SocUserAddRequest(expectedSocUser.firebaseId, expectedSocUser.name, expectedSocUser.email, expectedSocUser.imageUrl, false, false))
            user shouldBe expectedSocUser
        }

        "put user" {
            val updatedSocUser = SocUser(
                "moNoTwZcU5Nwg4qMBBVW9uJBQM12",
                "Gabushko",
                "gabriel@streetofcode.sk",
                "https://streetofcode.sk/wp-content/uploads/2020/04/7520735.png",
                true
            )
            val user = putUser(SocUserEditRequest(updatedSocUser.name, updatedSocUser.imageUrl, true))
            user.name shouldBe updatedSocUser.name

            val receivedUser = getUser()
            receivedUser.name shouldBe updatedSocUser.name
        }
    }

    private fun getUser(): SocUser {
        return restWithUserRole().getForEntity<SocUser>("/user").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun postUser(userRequest: SocUserAddRequest): SocUser {
        return restWithUserRole().postForEntity<SocUser>("/user", userRequest).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun putUser(userRequest: SocUserEditRequest): SocUser {
        return restWithUserRole().putForEntity<SocUser>("/user", userRequest).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun failGettingUser() {
        restWithUserRole().getForEntity<ResourceNotFoundException>("/user").statusCode shouldBe HttpStatus.NOT_FOUND
    }
}
