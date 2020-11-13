package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.db.projection.homepage.CoursesHomepage
import sk.streetofcode.courseplatformbackend.db.projection.overview.CourseOverview
import sk.streetofcode.courseplatformbackend.model.Course

interface CourseService {
    fun get(id: Long): Course
    fun getAll(): List<Course>
    fun add(addRequest: CourseAddRequest): Course
    fun edit(id: Long, editRequest: CourseEditRequest): Course
    fun delete(id: Long): Course
    fun getCoursesHomepage(): List<CoursesHomepage>
    fun getCourseOverview(id: Long): CourseOverview
}