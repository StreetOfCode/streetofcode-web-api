package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewDto
import sk.streetofcode.courseplatformbackend.model.CourseReview

@Component
class CourseReviewMapper {
    fun toCourseReviewDto(courseReview: CourseReview): CourseReviewDto {
        return CourseReviewDto(
            id = courseReview.id!!,
            courseId = courseReview.courseId,
            rating = courseReview.rating,
            text = courseReview.text,
            userName = courseReview.userName
        )
    }
}
