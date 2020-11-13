package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.db.projection.homepage.CoursesHomepage
import sk.streetofcode.courseplatformbackend.db.projection.overview.CourseOverview

interface CourseService {
    fun get(id: Long): CourseDto
    fun getAll(): List<CourseDto>
    fun add(addRequest: CourseAddRequest): CourseDto
    fun edit(id: Long, editRequest: CourseEditRequest): CourseDto
    fun delete(id: Long): CourseDto
    fun getCoursesHomepage(): List<CoursesHomepage>
    fun getCourseOverview(id: Long): CourseOverview
}