package sk.streetofcode.courseplatformbackend

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import javax.sql.DataSource

@SpringBootApplication
@ConfigurationPropertiesScan
class CoursePlatformBackendApplication : CommandLineRunner {
    @Autowired
    var dataSource: DataSource? = null
    override fun run(vararg args: String?) {
        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate()
    }
}

fun main(args: Array<String>) {
    runApplication<CoursePlatformBackendApplication>(*args)
}
