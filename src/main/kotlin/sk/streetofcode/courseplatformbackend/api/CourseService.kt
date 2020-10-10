package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.db.projection.CourseOverview
import sk.streetofcode.courseplatformbackend.db.projection.CoursesHomepageOverview
import sk.streetofcode.courseplatformbackend.model.Course

interface CourseService {
    fun get(id: Long): Course
    fun getAll(): List<Course>
    fun add(addRequest: CourseAddRequest): Long
    fun delete(id: Long)
    fun getCoursesHomepage(): List<CoursesHomepageOverview>
    fun getCourseOverview(id: Long): CourseOverview
}