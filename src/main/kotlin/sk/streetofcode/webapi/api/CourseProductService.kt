package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.api.dto.IsOwnedByUserDto
import sk.streetofcode.webapi.model.CourseProduct

interface CourseProductService {
    fun getAllForCourse(courseId: Long): List<CourseProductDto>
    fun get(courseProductId: String): CourseProduct
    fun isOwnedByUser(courseId: Long): IsOwnedByUserDto
}
