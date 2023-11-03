package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.CourseUserProduct

@Repository
interface CourseUserProductRepository : CrudRepository<CourseUserProduct, String> {
    fun findBySocUserFirebaseIdAndCourseProduct(
        firebaseId: String,
        courseProduct: CourseProduct
    ): List<CourseUserProduct>

    fun existsBySocUserFirebaseIdAndCourseProduct(
        firebaseId: String,
        courseProduct: CourseProduct
    ): Boolean
}
