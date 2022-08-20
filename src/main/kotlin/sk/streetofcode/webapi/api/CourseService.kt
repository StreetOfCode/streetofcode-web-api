package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.CourseDto
import sk.streetofcode.webapi.api.dto.CourseOverviewDto
import sk.streetofcode.webapi.api.request.CourseAddRequest
import sk.streetofcode.webapi.api.request.CourseEditRequest

interface CourseService {
    fun get(id: Long): CourseDto
    fun getAll(): List<CourseDto>
    fun add(addRequest: CourseAddRequest): CourseDto
    fun edit(id: Long, editRequest: CourseEditRequest): CourseDto
    fun delete(id: Long): CourseDto
    fun getPublicCoursesOverview(): List<CourseOverviewDto>
    fun getAllCoursesOverview(): List<CourseOverviewDto>
    fun getPublicCourseOverview(userId: String?, slug: String): CourseOverviewDto
    fun getAnyCourseOverview(userId: String?, slug: String): CourseOverviewDto
    fun getMyCourses(userId: String): List<CourseOverviewDto>
}
