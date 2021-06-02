package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.courseplatformbackend.api.request.CourseReviewAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseReviewEditRequest

interface CourseReviewService {
    fun getCourseReviews(courseId: Long): List<CourseReviewDto>
    fun getCourseReviewsOverview(courseId: Long): CourseReviewsOverviewDto
    fun get(id: Long): CourseReviewDto
    fun add(addRequest: CourseReviewAddRequest): CourseReviewDto
    fun edit(id: Long, editRequest: CourseReviewEditRequest): CourseReviewDto
    fun delete(id: Long): CourseReviewDto
}
