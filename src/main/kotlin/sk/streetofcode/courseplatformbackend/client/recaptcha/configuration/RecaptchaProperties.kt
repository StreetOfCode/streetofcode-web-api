package sk.streetofcode.courseplatformbackend.client.recaptcha.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("google.recaptcha")
class RecaptchaProperties {
    lateinit var secretKey: String
    lateinit var threshold: String
}
