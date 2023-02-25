package sk.streetofcode.webapi.integration

import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import sk.streetofcode.webapi.api.dto.CourseDto
import sk.streetofcode.webapi.api.dto.CourseOverviewDto
import sk.streetofcode.webapi.api.exception.AuthorizationException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.api.request.CourseAddRequest
import sk.streetofcode.webapi.api.request.CourseEditRequest
import sk.streetofcode.webapi.configuration.SpringBootTestAnnotation
import sk.streetofcode.webapi.model.CourseStatus

@SpringBootTestAnnotation
class CourseIntegrationTests : IntegrationTests() {
    init {
        "failed get courses" {
            getCoursesNotAuthenticatedReturns401()
            getCoursesWithUserRoleThrowsAuthorizationException()
        }

        "get courses overview with admin role" {
            val coursesResponse = getCoursesOverviewWithAdminRole()
            coursesResponse.size shouldBeGreaterThan 0
        }

        "get courses overview with user role" {
            val coursesResponse = getCoursesOverviewWithUserRole()
            coursesResponse.statusCode shouldBe HttpStatus.OK

            val courses = coursesResponse.body!!
            courses.size shouldBeGreaterThan 0
        }

        "get course overview" {
            val uniqueSlug = getRandomString()
            val course = addCourse(CourseAddRequest(1, 1, "testName", uniqueSlug, "short", "long", "resources", "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.DRAFT, 1))

            getCourseOverviewWithUserRoleThrowsAuthorizationException(uniqueSlug)

            val fetchedCourse = getCourseOverviewWithAdminRole(uniqueSlug)
            fetchedCourse.id shouldBe course.id
            fetchedCourse.name shouldBe "testName"
            fetchedCourse.slug shouldBe uniqueSlug
            fetchedCourse.shortDescription shouldBe "short"
            fetchedCourse.longDescription shouldBe "long"
            fetchedCourse.resources shouldBe "resources"
            fetchedCourse.trailerUrl shouldBe "trailerUrl"
            fetchedCourse.thumbnailUrl shouldBe "thumbnailUrl"
            fetchedCourse.iconUrl shouldBe "iconUrl"
            fetchedCourse.status shouldBe CourseStatus.DRAFT
            fetchedCourse.author.id shouldBe 1
            fetchedCourse.difficulty.id shouldBe 1
            fetchedCourse.courseOrder shouldBe 1
        }

        "add course" {
            val uniqueSlug = getRandomString()
            val course = addCourse(CourseAddRequest(1, 1, "testName", uniqueSlug, "short", "long", null, "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PRIVATE, 1))

            val fetchedCourse = getCourse(course.id)
            fetchedCourse.id shouldBe course.id
            fetchedCourse.name shouldBe "testName"
            fetchedCourse.slug shouldBe uniqueSlug
            fetchedCourse.shortDescription shouldBe "short"
            fetchedCourse.longDescription shouldBe "long"
            fetchedCourse.resources shouldBe null
            fetchedCourse.trailerUrl shouldBe "trailerUrl"
            fetchedCourse.thumbnailUrl shouldBe "thumbnailUrl"
            fetchedCourse.iconUrl shouldBe "iconUrl"
            fetchedCourse.status shouldBe CourseStatus.PRIVATE
            fetchedCourse.courseOrder shouldBe 1

            // get courses
            val coursesResponse = getCourses()
            coursesResponse.find { it.id == course.id } shouldBe course

            // get slugs
            val courseIdsResponse = getCourseSlugs()
            courseIdsResponse.statusCode shouldBe HttpStatus.OK

            val courseIds = courseIdsResponse.body!!
            courseIds.find { it == uniqueSlug } shouldBe uniqueSlug
        }

        "edit course" {
            val uniqueSlug = getRandomString()
            editCourseNotFound(999, CourseEditRequest(999, 1, 1, "editedTestName", uniqueSlug, "editedShort", "editedLong", "resources", "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PUBLIC, 1))

            val course = addCourse(CourseAddRequest(1, 1, "testName", uniqueSlug, "short", "long", "resources", "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PUBLIC, 1))

            val editedCourse = editCourse(
                course.id,
                CourseEditRequest(course.id, 1, 1, "editedTestName", "editedSlug", "editedShort", "editedLong", "editedResources", "editedTrailerUrl", "editedThumbnailUrl", "editedIconUrl", CourseStatus.PRIVATE, 2)
            )

            val fetchedCourse = getCourse(editedCourse.id)
            fetchedCourse.name shouldBe "editedTestName"
            fetchedCourse.slug shouldBe "editedSlug"
            fetchedCourse.shortDescription shouldBe "editedShort"
            fetchedCourse.longDescription shouldBe "editedLong"
            fetchedCourse.resources shouldBe "editedResources"
            fetchedCourse.trailerUrl shouldBe "editedTrailerUrl"
            fetchedCourse.thumbnailUrl shouldBe "editedThumbnailUrl"
            fetchedCourse.iconUrl shouldBe "editedIconUrl"
            fetchedCourse.status shouldBe CourseStatus.PRIVATE
            fetchedCourse.courseOrder shouldBe 2
        }

        "delete course" {
            val uniqueSlug = getRandomString()
            val course = addCourse(CourseAddRequest(1, 1, "testName", uniqueSlug, "short", "long", null, "trailerUrl", "trailerUrl", "thumbnailUrl", CourseStatus.PRIVATE, 1))

            val removedCourse = deleteCourse(course.id)
            removedCourse.shouldBe(course)

            getCourseNotFound(course.id)
        }

        "get my courses" {
            createRandomUser()

            val myCoursesResponse = getMyCourses()
            myCoursesResponse.statusCode shouldBe HttpStatus.OK

            val courses = myCoursesResponse.body!!
            courses.size shouldBe 0

            // update progress then try again
            updateProgress(1)

            val myCoursesAgainResponse = getMyCourses()
            myCoursesAgainResponse.statusCode shouldBe HttpStatus.OK

            val coursesAgain = myCoursesAgainResponse.body!!
            coursesAgain.size shouldBe 1
        }

        "course order" {
            getCoursesOverviewWithAdminRole().forEach { deleteCourse(it.id) }

            val emptyCoursesResponse = getCoursesOverviewWithAdminRole()
            emptyCoursesResponse.size shouldBe 0

            val courseOrder1 = 1
            val course1 = addCourse(CourseAddRequest(1, 1, "testName", "test1", "short", "long", null, "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PUBLIC, courseOrder1))

            val courseOrder2 = 2
            addCourse(CourseAddRequest(1, 1, "testName", "test2", "short", "long", null, "trailerUrl", "thumbnailUrl", "iconUrl", CourseStatus.PUBLIC, courseOrder2))

            val courses = getCoursesOverviewWithAdminRole()
            courses.size shouldBe 2
            courses[0].courseOrder shouldBe 1
            courses[1].courseOrder shouldBe 2

            val courseOrder3 = 3
            editCourse(
                course1.id,
                CourseEditRequest(course1.id, 1, 1, "editedTestName", "editedSlug", "editedShort", "editedLong", "editedResources", "editedTrailerUrl", "editedThumbnailUrl", "editedIconUrl", CourseStatus.PRIVATE, courseOrder3)
            )

            val coursesUpdated = getCoursesOverviewWithAdminRole()
            coursesUpdated.size shouldBe 2
            coursesUpdated[0].courseOrder shouldBe 2
            coursesUpdated[1].courseOrder shouldBe 3
            coursesUpdated[1].id shouldBe course1.id
        }
    }

    private fun getCourses(): List<CourseDto> {
        return restWithAdminRole().getForEntity<Array<CourseDto>>("/course").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    private fun getCourseSlugs(): ResponseEntity<List<String>> {
        return restWithAdminRole().getForEntity("/course/slug")
    }

    private fun getCoursesWithUserRoleThrowsAuthorizationException() {
        return restWithUserRole().getForEntity<Unit>("/course").statusCode shouldBe HttpStatus.FORBIDDEN
    }

    private fun getCoursesNotAuthenticatedReturns401() {
        return restTemplate.getForEntity<Unit>("/course").statusCode shouldBe HttpStatus.UNAUTHORIZED
    }

    private fun getCoursesOverviewWithAdminRole(): List<CourseOverviewDto> {
        return restWithAdminRole().getForEntity<Array<CourseOverviewDto>>("/course/overview").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!.toList()
        }
    }

    private fun getCoursesOverviewWithUserRole(): ResponseEntity<List<CourseOverviewDto>> {
        return restWithUserRole().getForEntity("/course/overview")
    }

    private fun getMyCourses(): ResponseEntity<List<CourseOverviewDto>> {
        return restWithUserRole().getForEntity("/course/my-courses")
    }

    private fun getCourse(courseId: Long): CourseDto {
        return restWithAdminRole().getForEntity<CourseDto>("/course/$courseId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getCourseOverviewWithAdminRole(slug: String): CourseOverviewDto {
        return restWithAdminRole().getForEntity<CourseOverviewDto>("/course/overview/$slug").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun getCourseOverviewWithUserRoleThrowsAuthorizationException(slug: String) {
        return restWithUserRole().getForEntity<AuthorizationException>("/course/overview/$slug").statusCode shouldBe HttpStatus.FORBIDDEN
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

    private fun updateProgress(lectureId: Long) {
        return restWithUserRole().postForEntity<Void>("/progress/update/$lectureId").statusCode shouldBe HttpStatus.OK
    }
}
