package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseMyDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest

interface CourseService {
    fun get(id: Long): CourseDto
    fun getAll(): List<CourseDto>
    fun add(addRequest: CourseAddRequest): CourseDto
    fun edit(id: Long, editRequest: CourseEditRequest): CourseDto
    fun delete(id: Long): CourseDto
    fun getPublicCoursesOverview(): List<CourseOverviewDto>
    fun getAllCoursesOverview(): List<CourseOverviewDto>
    fun getPublicCourseOverview(userId: String?, id: Long): CourseOverviewDto
    fun getAnyCourseOverview(userId: String?, id: Long): CourseOverviewDto
    fun getMyCourses(userId: String): List<CourseMyDto>
}
