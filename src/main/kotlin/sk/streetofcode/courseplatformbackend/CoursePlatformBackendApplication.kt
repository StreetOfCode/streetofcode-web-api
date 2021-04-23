package sk.streetofcode.courseplatformbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class CoursePlatformBackendApplication

fun main(args: Array<String>) {
    runApplication<CoursePlatformBackendApplication>(*args)
}
