package sk.streetofcode.webapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class StreetOfCodeWebApiApplication

fun main(args: Array<String>) {
    runApplication<StreetOfCodeWebApiApplication>(*args)
}
