package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseHomepageDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseMyDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import java.util.UUID

interface CourseService {
    fun get(id: Long): CourseDto
    fun getAll(): List<CourseDto>
    fun add(addRequest: CourseAddRequest): CourseDto
    fun edit(id: Long, editRequest: CourseEditRequest): CourseDto
    fun delete(id: Long): CourseDto
    fun getPublicCoursesHomepage(): List<CourseHomepageDto>
    fun getAllCoursesHomepage(): List<CourseHomepageDto>
    fun getPublicCourseOverview(userId: UUID?, id: Long): CourseOverviewDto
    fun getAnyCourseOverview(userId: UUID?, id: Long): CourseOverviewDto
    fun getMyCourses(userId: UUID): List<CourseMyDto>
}
