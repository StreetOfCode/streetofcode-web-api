package sk.streetofcode.courseplatformbackend.integration

import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.CourseReviewAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseReviewEditRequest
import sk.streetofcode.courseplatformbackend.configuration.SpringBootTestAnnotation

@SpringBootTestAnnotation
class CourseReviewIntegrationTests : IntegrationTests() {
    init {
        "get reviews" {
            forAll(row(1L), row(2L)) { courseId ->
                val courseReviews = getCourseReviews(courseId)
                courseReviews.size shouldBe 2
                courseReviews.forEach { it.courseId shouldBe courseId }
            }
        }

        "fail getting reviews - non-existing course" {
            restWithUserRole().getForEntity<ResourceNotFoundException>("/course-review/course/0").statusCode shouldBe HttpStatus.NOT_FOUND
        }

        "get review overview" {
            forAll(
                row(1L, 2.5, 2),
                row(2L, 2, 2)
            ) { courseId, expectedAverageRating, expectedNumberOfReviews ->
                val courseReviewOverview = getCourseReviewOverview(courseId)
                courseReviewOverview.averageRating shouldBe expectedAverageRating
                courseReviewOverview.numberOfReviews shouldBe expectedNumberOfReviews
            }
        }

        "fail getting course review overview - non-existing course" {
            restWithUserRole().getForEntity<ResourceNotFoundException>("/course-review/course/0/overview").statusCode shouldBe HttpStatus.NOT_FOUND
        }

        "get course review" {
            createRandomUser()

            val addedReview = addCourseReview(CourseReviewAddRequest(1, 4.5, "testText"))

            val receivedReview = getCourseReview(addedReview.id)
            receivedReview shouldBe addedReview
        }

        "add course review" {
            createRandomUser()

            val courseId = 1L
            val courseReviewsBefore = getCourseReviews(courseId)
            val courseReviewOverviewBefore = getCourseReviewOverview(courseId)

            val addedReview = addCourseReview(CourseReviewAddRequest(courseId, 4.0, "testText"))

            val courseReviewsAfter = getCourseReviews(courseId)

            courseReviewsAfter.size shouldBe courseReviewsBefore.size + 1
            addedReview shouldBeIn courseReviewsAfter

            val expectedAverage = addValueToAverage(
                courseReviewOverviewBefore.averageRating,
                courseReviewOverviewBefore.numberOfReviews,
                addedReview.rating
            )

            val courseReviewOverviewAfter = getCourseReviewOverview(courseId)
            courseReviewOverviewAfter.numberOfReviews shouldBe courseReviewOverviewBefore.numberOfReviews + 1
            courseReviewOverviewAfter.averageRating shouldBe expectedAverage
        }

        "fail adding course review - non-existing course" {
            failAddingCourseReview(CourseReviewAddRequest(0, 0.0, ""), HttpStatus.NOT_FOUND)
        }

        "fail adding course review - invalid rating" {
            failAddingCourseReview(CourseReviewAddRequest(1, 6.0, ""), HttpStatus.BAD_REQUEST)
        }

        "fail adding course review - user already added review for given course" {
            failAddingCourseReview(CourseReviewAddRequest(1, 5.0, ""), HttpStatus.BAD_REQUEST)
        }

        "edit course review" {
            createRandomUser()

            val courseId = 1L

            val reviewToEdit = addCourseReview(CourseReviewAddRequest(courseId, 4.0, "testText"))
            val courseReviewOverviewBefore = getCourseReviewOverview(courseId)

            editCourseReview(reviewToEdit.id, CourseReviewEditRequest(4.5, "updatedText"))

            val editedReview = getCourseReview(reviewToEdit.id)
            editedReview.id shouldBe reviewToEdit.id
            editedReview.courseId shouldBe reviewToEdit.courseId
            editedReview.rating shouldBe 4.5
            editedReview.text shouldBe "updatedText"

            val expectedNewAverage = replaceValueInAverage(
                courseReviewOverviewBefore.averageRating,
                courseReviewOverviewBefore.numberOfReviews,
                reviewToEdit.rating,
                editedReview.rating
            )

            val courseReviewsOverviewAfter = getCourseReviewOverview(courseId)
            courseReviewsOverviewAfter.numberOfReviews shouldBe courseReviewOverviewBefore.numberOfReviews
            courseReviewsOverviewAfter.averageRating shouldBe expectedNewAverage
        }

        "fail editing course review - non-existing review" {
            failEditingCourseReview(0, CourseReviewEditRequest(0.0, ""), HttpStatus.NOT_FOUND)
        }

        "fail editing course review - invalid rating" {
            failEditingCourseReview(1, CourseReviewEditRequest(6.0, ""), HttpStatus.BAD_REQUEST)
        }

        "fail editing course review - review not created by user" {
            failEditingCourseReview(2, CourseReviewEditRequest(5.0, ""), HttpStatus.FORBIDDEN)
        }

        "delete course review" {
            createRandomUser()

            val courseId = 1L

            val reviewToBeDeleted = addCourseReview(CourseReviewAddRequest(courseId, 4.0, "testText"))
            val courseReviewOverviewBefore = getCourseReviewOverview(courseId)

            val deletedReview = deleteCourseReview(reviewToBeDeleted.id)

            val courseReviewsAfter = getCourseReviews(courseId)
            deletedReview shouldNotBeIn courseReviewsAfter

            val expectedNewAverage = removeValueFromAverage(
                courseReviewOverviewBefore.averageRating,
                courseReviewOverviewBefore.numberOfReviews,
                reviewToBeDeleted.rating
            )

            val courseReviewsOverviewAfter = getCourseReviewOverview(courseId)
            courseReviewsOverviewAfter.numberOfReviews shouldBe courseReviewOverviewBefore.numberOfReviews - 1
            courseReviewsOverviewAfter.averageRating shouldBe expectedNewAverage
        }

        "fail deleting course review - non-existing review" {
            failDeletingCourseReview(0, HttpStatus.NOT_FOUND)
        }

        "fail deleting course review - review not created by user" {
            failDeletingCourseReview(2, HttpStatus.FORBIDDEN)
        }
    }

    private fun getCourseReviews(courseId: Long, expectedStatus: HttpStatus = HttpStatus.OK): List<CourseReviewDto> {
        return restWithUserRole().getForEntity<Array<CourseReviewDto>>("/course-review/course/$courseId").let {
            it.statusCode shouldBe expectedStatus
            it.body!!.toList()
        }
    }

    private fun getCourseReview(reviewId: Long, expectedStatus: HttpStatus = HttpStatus.OK): CourseReviewDto {
        return restWithUserRole().getForEntity<CourseReviewDto>("/course-review/$reviewId").let {
            it.statusCode shouldBe expectedStatus
            it.body!!
        }
    }

    private fun getCourseReviewOverview(
        courseId: Long,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): CourseReviewsOverviewDto {
        return restWithUserRole().getForEntity<CourseReviewsOverviewDto>("/course-review/course/$courseId/overview")
            .let {
                it.statusCode shouldBe expectedStatus
                it.body!!
            }
    }

    private fun addCourseReview(request: CourseReviewAddRequest): CourseReviewDto {
        return restWithUserRole().postForEntity<CourseReviewDto>("/course-review", request).let {
            it.statusCode shouldBe HttpStatus.CREATED
            it.body!!
        }
    }

    private fun failAddingCourseReview(request: CourseReviewAddRequest, expectedStatus: HttpStatus) {
        restWithUserRole().postForEntity<Unit>("/course-review", request).statusCode shouldBe expectedStatus
    }

    private fun editCourseReview(courseReviewId: Long, request: CourseReviewEditRequest): CourseReviewDto {
        return restWithUserRole().putForEntity<CourseReviewDto>("/course-review/$courseReviewId", request).let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun failEditingCourseReview(
        courseReviewId: Long,
        request: CourseReviewEditRequest,
        expectedStatus: HttpStatus
    ) {
        restWithUserRole().putForEntity<Unit>(
            "/course-review/$courseReviewId",
            request
        ).statusCode shouldBe expectedStatus
    }

    private fun deleteCourseReview(courseReviewId: Long): CourseReviewDto {
        return restWithUserRole().deleteForEntity<CourseReviewDto>("/course-review/$courseReviewId").let {
            it.statusCode shouldBe HttpStatus.OK
            it.body!!
        }
    }

    private fun failDeletingCourseReview(courseReviewId: Long, expectedStatus: HttpStatus) {
        restWithUserRole().deleteForEntity<Unit>("/course-review/$courseReviewId").statusCode shouldBe expectedStatus
    }

    // formula from https://math.stackexchange.com/a/957376
    private fun addValueToAverage(originalAverage: Double, count: Long, value: Double) =
        originalAverage + ((value - originalAverage) / (count + 1))

    // formula from https://math.stackexchange.com/a/1567345
    private fun removeValueFromAverage(originalAverage: Double, count: Long, value: Double) =
        (originalAverage * count - value) / (count - 1)

    // used to calculate new expected average rating - I found no other way to test it and it seems like a
    // reasonable thing to test
    private fun replaceValueInAverage(
        originalAverage: Double,
        count: Long,
        oldValue: Double,
        newValue: Double
    ): Double {
        // we first add the new value to the average
        val newAverageWithNewValueAdded = addValueToAverage(originalAverage, count, newValue)
        // then we subtract the original value from the new average
        return removeValueFromAverage(newAverageWithNewValueAdded, count + 1, oldValue)
    }
}
