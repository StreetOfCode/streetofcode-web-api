package sk.streetofcode.courseplatformbackend.configuration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TestSecurityConfiguration::class])
@ActiveProfiles("test")
annotation class SpringBootTestAnnotation
