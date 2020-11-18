package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseHomepageDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.model.Course
import sk.streetofcode.courseplatformbackend.model.CourseStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseIntegrationTests : IntegrationTests() {
    init {
        "get courses" {
            val coursesResponse = getCourses()
            coursesResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = coursesResponse.headers["Content-Range"]
            contentRange shouldBe listOf("course 0-2/2")

            val courses = coursesResponse.body!!
            courses.size shouldBe 2
        }

        "get courses for homepage" {
            val coursesResponse = getCoursesHomepage()
            coursesResponse.statusCode shouldBe HttpStatus.OK

            val courses = coursesResponse.body!!
            courses.size shouldBe 2
        }

        "get course overview" {

            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", "imageUrl", CourseStatus.DRAFT))

            val fetchedCourse = getCourseOverview(course.id)
            fetchedCourse.id shouldBe course.id
            fetchedCourse.name shouldBe "testName"
            fetchedCourse.shortDescription shouldBe "short"
            fetchedCourse.longDescription shouldBe "long"
            fetchedCourse.imageUrl shouldBe "imageUrl"
            fetchedCourse.status shouldBe CourseStatus.DRAFT
            fetchedCourse.author!!.id shouldBe 1
            fetchedCourse.difficulty!!.id shouldBe 1
        }

        "add course" {
            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", "imageUrl", CourseStatus.PRIVATE))

            val fetchedCourse = getCourse(course.id)
            fetchedCourse.id shouldBe course.id
            fetchedCourse.name shouldBe "testName"
            fetchedCourse.shortDescription shouldBe "short"
            fetchedCourse.longDescription shouldBe "long"
            fetchedCourse.imageUrl shouldBe "imageUrl"
            fetchedCourse.status shouldBe CourseStatus.PRIVATE
        }

        "edit course" {
            editCourseNotFound(999, CourseEditRequest(999, 1, 1, "editedTestName", "editedShort", "editedLong", "imageUrl", CourseStatus.PUBLIC))

            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", "imageUrl", CourseStatus.PUBLIC))

            val editedCourse = editCourse(
                    course.id,
                    CourseEditRequest(course.id, 1, 1, "editedTestName", "editedShort", "editedLong", "editedImageUrl", CourseStatus.PRIVATE)
            )

            val fetchedCourse = getCourse(editedCourse.id!!)
            fetchedCourse.name shouldBe "editedTestName"
            fetchedCourse.shortDescription shouldBe "editedShort"
            fetchedCourse.longDescription shouldBe "editedLong"
            fetchedCourse.imageUrl shouldBe "editedImageUrl"
            fetchedCourse.status shouldBe CourseStatus.PRIVATE
        }

        "delete course" {
            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", "imageUrl", CourseStatus.PRIVATE))

            val removedCourse = deleteCourse(course.id)
            removedCourse.shouldBe(course)

            getCourseNotFound(course.id)
        }
    }


    private fun getCourses(): ResponseEntity<List<CourseDto>> {
        return restTemplate.getForEntity("/course")
    }

    private fun getCoursesHomepage(): ResponseEntity<List<CourseHomepageDto>> {
        return restTemplate.getForEntity("/course/home-page")
    }

    private fun getCourse(courseId: Long): CourseDto {
        return restTemplate.getForEntity<CourseDto>("/course/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getCourseOverview(courseId: Long): CourseOverviewDto {
        return restTemplate.getForEntity<CourseOverviewDto>("/course/overview/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getCourseNotFound(courseId: Long) {
        return restTemplate.getForEntity<ResourceNotFoundException>("/course/$courseId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editCourseNotFound(courseId: Long, body: CourseEditRequest) {
        return restTemplate.putForEntity<ResourceNotFoundException>("/course/$courseId", body).let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun addCourse(body: CourseAddRequest): CourseDto {
        return restTemplate.postForEntity<CourseDto>("/course", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editCourse(courseId: Long, body: CourseEditRequest): Course {
        return restTemplate.putForEntity<Course>("/course/$courseId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteCourse(courseId: Long): CourseDto {
        return restTemplate.deleteForEntity<CourseDto>("/course/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
