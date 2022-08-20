package sk.streetofcode.webapi.client.recaptcha

import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import sk.streetofcode.webapi.client.recaptcha.configuration.RecaptchaProperties

@Service
class RecaptchaApiClient(
    @Qualifier("recaptchaRestTemplate") private val restTemplate: RestTemplate,
    private val recaptchaProperties: RecaptchaProperties
) {

    companion object {
        private val log = LoggerFactory.getLogger(RecaptchaApiClient::class.java)
    }

    /***
     * Returns true or false based on token validity
     */
    fun verifyRecaptchaToken(token: String): Boolean {
        val response =
            restTemplate.getForEntity<String>("/siteverify?secret=${recaptchaProperties.secretKey}&response=$token")
        return isHuman(response.body!!)
    }

    private fun isHuman(responseJson: String): Boolean {
        val score = getScore(responseJson)
        return (score > recaptchaProperties.threshold.toFloat())
    }

    private fun getScore(responseJson: String): Float {
        return (JSONObject(responseJson).getFloat("score"))
    }
}
