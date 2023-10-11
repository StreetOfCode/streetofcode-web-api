package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.UserProduct

@Repository
interface UserProductRepository : CrudRepository<UserProduct, String> {
    fun findBySocUserFirebaseIdAndCourseProduct(firebaseId: String, courseProduct: CourseProduct): List<UserProduct>
}
