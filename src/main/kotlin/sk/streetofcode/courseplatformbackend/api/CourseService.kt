package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.model.Course

interface CourseService {
    fun get(id: Long): Course
    fun getAll(): List<Course>
    fun add(addRequest: CourseAddRequest): Long
    fun delete(id: Long)
}