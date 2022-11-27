package sk.streetofcode.webapi.client.convertkit

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ConvertKitApiClientTest {

    @Test
    fun getFirstNameTest() {
        ConvertKitApiClient.getFirstName(null) shouldBe null
        ConvertKitApiClient.getFirstName("") shouldBe ""
        ConvertKitApiClient.getFirstName("name") shouldBe "name"
        ConvertKitApiClient.getFirstName("first second") shouldBe "first"
        ConvertKitApiClient.getFirstName("first second third") shouldBe "first"
        ConvertKitApiClient.getFirstName("1name_ds!!!") shouldBe "1name_ds!!!"
        ConvertKitApiClient.getFirstName("first       second") shouldBe "first"
    }
}
