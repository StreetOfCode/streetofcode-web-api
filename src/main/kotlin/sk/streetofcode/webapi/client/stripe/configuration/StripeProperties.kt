package sk.streetofcode.webapi.client.stripe.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("stripe")
class StripeProperties {
    lateinit var apiKey: String
    lateinit var webhookSecret: String
}
