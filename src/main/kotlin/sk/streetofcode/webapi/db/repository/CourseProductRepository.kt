package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.CourseProduct

@Repository
interface CourseProductRepository : CrudRepository<CourseProduct, String> {
    fun findAllByCourseId(courseId: Long): List<CourseProduct>
}
