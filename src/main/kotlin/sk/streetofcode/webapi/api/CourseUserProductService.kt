package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.CourseUserProduct

interface CourseUserProductService {
    fun hasUserProduct(userId: String, courseProductId: String): Boolean
    fun getProductCourseUserProducts(userId: String, courseProduct: CourseProduct): List<CourseUserProduct>
    fun addCourseUserProduct(
        userId: String,
        courseProductId: String,
        finalAmount: Long,
        promoCode: String?
    ): CourseUserProduct
}
