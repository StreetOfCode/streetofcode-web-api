package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.CourseReviewDto
import sk.streetofcode.webapi.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.webapi.api.request.CourseReviewAddRequest
import sk.streetofcode.webapi.api.request.CourseReviewEditRequest

interface CourseReviewService {
    fun getCourseReviews(courseId: Long): List<CourseReviewDto>
    fun getCourseReviewsOverview(courseId: Long): CourseReviewsOverviewDto
    fun get(id: Long): CourseReviewDto
    fun add(addRequest: CourseReviewAddRequest): CourseReviewDto
    fun edit(id: Long, editRequest: CourseReviewEditRequest): CourseReviewDto
    fun delete(id: Long): CourseReviewDto
}
