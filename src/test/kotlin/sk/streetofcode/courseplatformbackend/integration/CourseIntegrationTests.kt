package sk.streetofcode.courseplatformbackend.integration

import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.exception.AuthorizationException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.configuration.SpringBootTestAnnotation
import sk.streetofcode.courseplatformbackend.model.CourseStatus

@SpringBootTestAnnotation
class CourseIntegrationTests : IntegrationTests() {
    init {
        "get courses" {

            getCoursesNotAuthenticatedReturns401()
            getCoursesWithUserRoleThrowsAuthorizationException()

            val coursesResponse = getCourses()
            coursesResponse.statusCode shouldBe HttpStatus.OK
            val contentRange = coursesResponse.headers["Content-Range"]
            contentRange shouldBe listOf("course 0-2/2")

            val courses = coursesResponse.body!!
            courses.size shouldBe 2
        }

        "get courses overview with admin role" {
            val coursesResponse = getCoursesOverviewWithAdminRole()
            coursesResponse.statusCode shouldBe HttpStatus.OK

            val courses = coursesResponse.body!!
            courses.size shouldBe 2
        }

        "get courses overview with user role" {
            val coursesResponse = getCoursesOverviewWithUserRole()
            coursesResponse.statusCode shouldBe HttpStatus.OK

            val courses = coursesResponse.body!!
            courses.size shouldBe 1
        }

        "get course overview" {

            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", "resources", "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.DRAFT))

            getCourseOverviewWithUserRoleThrowsAuthorizationException(course.id)

            val fetchedCourse = getCourseOverviewWithAdminRole(course.id)
            fetchedCourse.id shouldBe course.id
            fetchedCourse.name shouldBe "testName"
            fetchedCourse.shortDescription shouldBe "short"
            fetchedCourse.longDescription shouldBe "long"
            fetchedCourse.resources shouldBe "resources"
            fetchedCourse.trailerUrl shouldBe "trailerUrl"
            fetchedCourse.thumbnailUrl shouldBe "thumbnailUrl"
            fetchedCourse.iconUrl shouldBe "iconUrl"
            fetchedCourse.status shouldBe CourseStatus.DRAFT
            fetchedCourse.author!!.id shouldBe 1
            fetchedCourse.difficulty!!.id shouldBe 1
        }

        "add course" {
            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", null, "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PRIVATE))

            val fetchedCourse = getCourse(course.id)
            fetchedCourse.id shouldBe course.id
            fetchedCourse.name shouldBe "testName"
            fetchedCourse.shortDescription shouldBe "short"
            fetchedCourse.longDescription shouldBe "long"
            fetchedCourse.resources shouldBe null
            fetchedCourse.trailerUrl shouldBe "trailerUrl"
            fetchedCourse.thumbnailUrl shouldBe "thumbnailUrl"
            fetchedCourse.iconUrl shouldBe "iconUrl"
            fetchedCourse.status shouldBe CourseStatus.PRIVATE
        }

        "edit course" {
            editCourseNotFound(999, CourseEditRequest(999, 1, 1, "editedTestName", "editedShort", "editedLong", "resources", "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PUBLIC))

            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", "resources", "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PUBLIC))

            val editedCourse = editCourse(
                course.id,
                CourseEditRequest(course.id, 1, 1, "editedTestName", "editedShort", "editedLong", "editedResources", "editedTrailerUrl", "editedThumbnailUrl", "editedIconUrl", CourseStatus.PRIVATE)
            )

            val fetchedCourse = getCourse(editedCourse.id)
            fetchedCourse.name shouldBe "editedTestName"
            fetchedCourse.shortDescription shouldBe "editedShort"
            fetchedCourse.longDescription shouldBe "editedLong"
            fetchedCourse.resources shouldBe "editedResources"
            fetchedCourse.trailerUrl shouldBe "editedTrailerUrl"
            fetchedCourse.thumbnailUrl shouldBe "editedThumbnailUrl"
            fetchedCourse.iconUrl shouldBe "editedIconUrl"
            fetchedCourse.status shouldBe CourseStatus.PRIVATE
        }

        "delete course" {
            val course = addCourse(CourseAddRequest(1, 1, "testName", "short", "long", null, "trailerUrl", "trailerUrl", "thumbnailUrl", CourseStatus.PRIVATE))

            val removedCourse = deleteCourse(course.id)
            removedCourse.shouldBe(course)

            getCourseNotFound(course.id)
        }
    }

    private fun getCourses(): ResponseEntity<List<CourseDto>> {
        return restWithAdminRole().getForEntity("/course")
    }

    private fun getCoursesWithUserRoleThrowsAuthorizationException() {
        return restWithUserRole().getForEntity<Unit>("/course").statusCode shouldBe HttpStatus.FORBIDDEN
    }

    private fun getCoursesNotAuthenticatedReturns401() {
        return restTemplate.getForEntity<Unit>("/course").statusCode shouldBe HttpStatus.UNAUTHORIZED
    }

    private fun getCoursesOverviewWithAdminRole(): ResponseEntity<List<CourseOverviewDto>> {
        return restWithAdminRole().getForEntity("/course/overview")
    }

    private fun getCoursesOverviewWithUserRole(): ResponseEntity<List<CourseOverviewDto>> {
        return restWithUserRole().getForEntity("/course/overview")
    }

    private fun getCourse(courseId: Long): CourseDto {
        return restWithAdminRole().getForEntity<CourseDto>("/course/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getCourseOverviewWithAdminRole(courseId: Long): CourseOverviewDto {
        return restWithAdminRole().getForEntity<CourseOverviewDto>("/course/overview/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getCourseOverviewWithUserRoleThrowsAuthorizationException(courseId: Long) {
        return restWithUserRole().getForEntity<AuthorizationException>("/course/overview/$courseId").statusCode shouldBe HttpStatus.FORBIDDEN
    }

    private fun getCourseNotFound(courseId: Long) {
        return restWithAdminRole().getForEntity<ResourceNotFoundException>("/course/$courseId").let {
            it.statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    private fun editCourseNotFound(courseId: Long, body: CourseEditRequest) {
        return restWithAdminRole().putForEntity<ResourceNotFoundException>("/course/$courseId", body).statusCode shouldBe HttpStatus.NOT_FOUND
    }

    private fun addCourse(body: CourseAddRequest): CourseDto {
        return restWithAdminRole().postForEntity<CourseDto>("/course", body).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun editCourse(courseId: Long, body: CourseEditRequest): CourseDto {
        return restWithAdminRole().putForEntity<CourseDto>("/course/$courseId", body).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun deleteCourse(courseId: Long): CourseDto {
        return restWithAdminRole().deleteForEntity<CourseDto>("/course/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }
}
