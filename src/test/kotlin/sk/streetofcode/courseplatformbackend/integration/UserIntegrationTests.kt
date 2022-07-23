package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.UserAddRequest
import sk.streetofcode.courseplatformbackend.api.request.UserEditRequest
import sk.streetofcode.courseplatformbackend.configuration.SpringBootTestAnnotation
import sk.streetofcode.courseplatformbackend.model.User

@SpringBootTestAnnotation
class UserIntegrationTests : IntegrationTests() {
    init {
        "get user" {
            val fetchedUser = getUser()
            fetchedUser.firebaseId shouldBe "moNoTwZcU5Nwg4qMBBVW9uJBQM12"
            fetchedUser.name shouldBe "Gabriel Kerekeš"
            fetchedUser.email shouldBe "gabriel@streetofcode.sk"
            fetchedUser.imageUrl shouldBe "https://streetofcode.sk/wp-content/uploads/2020/04/7520735.png"

            setUserId("Dk71hPkR9Fc6SJma3S1NvGcrkHe2")
            val anotherFetchedUser = getUser()
            anotherFetchedUser.firebaseId shouldBe "Dk71hPkR9Fc6SJma3S1NvGcrkHe2"
            anotherFetchedUser.name shouldBe "Jakub Jahič"
            anotherFetchedUser.email shouldBe "jakub@streetofcode.sk"
            anotherFetchedUser.imageUrl shouldBe "https://streetofcode.sk/wp-content/uploads/2019/04/JFinal-768x576.jpg"

            setUserId("non-existing-user-id")
            failGettingUser()
        }

        "post user" {
            val newUserId = "QgXv1QVvoYZF6W46pwH51PzpJx73"
            setUserId(newUserId)
            val expectedUser = User(
                newUserId,
                "John Bool",
                "john.bool.bool@gmail.com",
                "wtf",
                false
            )
            val user = postUser(UserAddRequest(expectedUser.firebaseId, expectedUser.name, expectedUser.email, expectedUser.imageUrl, false, false))
            user shouldBe expectedUser
        }

        "put user" {
            val updatedUser = User(
                "moNoTwZcU5Nwg4qMBBVW9uJBQM12",
                "Gabushko",
                "gabriel@streetofcode.sk",
                "https://streetofcode.sk/wp-content/uploads/2020/04/7520735.png",
                true
            )
            val user = putUser(UserEditRequest(updatedUser.name, updatedUser.imageUrl, true))
            user.name shouldBe updatedUser.name

            val receivedUser = getUser()
            receivedUser.name shouldBe updatedUser.name
        }
    }

    private fun getUser(): User {
        return restWithUserRole().getForEntity<User>("/user").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun postUser(userRequest: UserAddRequest): User {
        return restWithUserRole().postForEntity<User>("/user", userRequest).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun putUser(userRequest: UserEditRequest): User {
        return restWithUserRole().putForEntity<User>("/user", userRequest).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun failGettingUser() {
        restWithUserRole().getForEntity<ResourceNotFoundException>("/user").statusCode shouldBe HttpStatus.NOT_FOUND
    }
}
