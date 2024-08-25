package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.api.dto.IsOwnedByUserDto

interface CourseProductService {
    fun getAllForCourse(courseId: Long): List<CourseProductDto>
    fun isOwnedByUser(courseId: Long): IsOwnedByUserDto
}
