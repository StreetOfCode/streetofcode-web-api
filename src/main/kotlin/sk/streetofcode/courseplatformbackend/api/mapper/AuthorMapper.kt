package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.AuthorCourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.AuthorOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Course

@Component
class AuthorMapper() {

    fun toAuthorOverview(author: Author, courseToCourseReviewOverview: List<Pair<Course, CourseReviewsOverviewDto>>): AuthorOverviewDto {
        return AuthorOverviewDto(
            author.id!!,
            author.name,
            author.url,
            author.description,
            courseToCourseReviewOverview.map { courseWithCourseOverview -> toAuthorCourseOverviewDto(courseWithCourseOverview) }
        )
    }

    private fun toAuthorCourseOverviewDto(courseWithCourseOverview: Pair<Course, CourseReviewsOverviewDto>): AuthorCourseOverviewDto {
        val (course, overview) = courseWithCourseOverview
        return AuthorCourseOverviewDto(
            course.id!!,
            course.difficulty,
            course.name,
            course.shortDescription,
            course.longDescription,
            course.imageUrl,
            overview
        )
    }
}
