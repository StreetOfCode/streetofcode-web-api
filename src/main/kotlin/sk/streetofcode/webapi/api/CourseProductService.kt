package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.api.dto.IsOwnedByUserDto
import sk.streetofcode.webapi.model.CourseProduct

interface CourseProductService {
    fun getAllForCourse(userId: String?, courseId: Long): List<CourseProductDto>
    fun get(courseProductId: String): CourseProduct
    fun isOwnedByUser(userId: String, courseId: Long): IsOwnedByUserDto
}
