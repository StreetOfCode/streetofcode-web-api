package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.UserProduct

interface UserProductService {
    fun getProductUserProducts(userId: String, courseProduct: CourseProduct): List<UserProduct>
    fun addUserProduct(userId: String, courseProductId: String, priceId: String): UserProduct
}
