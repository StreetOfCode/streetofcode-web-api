package sk.streetofcode.webapi.client.convertkit.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.properties.Delegates

@ConfigurationProperties("convertkit")
class ConvertKitProperties {
    lateinit var apiKey: String
    lateinit var formId: String
    var enabled by Delegates.notNull<Boolean>()
}
