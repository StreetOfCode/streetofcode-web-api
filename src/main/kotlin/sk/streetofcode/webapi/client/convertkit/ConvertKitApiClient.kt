package sk.streetofcode.webapi.client.convertkit

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import sk.streetofcode.webapi.client.convertkit.configuration.ConvertKitProperties

@Service
class ConvertKitApiClient(
    @Qualifier("convertKitRestTemplate") private val restTemplate: RestTemplate,
    private val convertKitProperties: ConvertKitProperties
) {

    companion object {
        private val log = LoggerFactory.getLogger(ConvertKitApiClient::class.java)

        fun getFirstName(name: String?): String? {
            if (name !== null) {
                return name.split(" ")[0]
            }

            return null
        }
    }

    fun addSubscriber(email: String, name: String?) {
        val addRequest = AddEmailRequest(
            api_key = convertKitProperties.apiKey,
            email,
            first_name = getFirstName(name)
        )
        val response = restTemplate.postForEntity("/forms/${convertKitProperties.formId}/subscribe", addRequest, String::class.java)
        if (!response.statusCode.is2xxSuccessful) {
            log.info("Add subscriber request didn't finished successfully, response: {}", response)
        }
    }
}

data class AddEmailRequest(
    val api_key: String,
    val email: String,
    val first_name: String?
)
