package sk.streetofcode.webapi.service

import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.SocUserService
import sk.streetofcode.webapi.api.CourseUserProductService
import sk.streetofcode.webapi.db.repository.CourseProductRepository
import sk.streetofcode.webapi.db.repository.CourseUserProductRepository
import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.CourseUserProduct
import java.time.OffsetDateTime

@Service
class CourseUserProductServiceImpl(
    val courseUserProductRepository: CourseUserProductRepository,
    val courseProductRepository: CourseProductRepository,
    val userService: SocUserService
) : CourseUserProductService {
    override fun getProductCourseUserProducts(userId: String, courseProduct: CourseProduct): List<CourseUserProduct> =
        courseUserProductRepository.findBySocUserFirebaseIdAndCourseProduct(userId, courseProduct)

    override fun addCourseUserProduct(userId: String, courseProductId: String, promoCode: String): CourseUserProduct {
        val user = userService.get(userId)
        val courseProduct = courseProductRepository.findById(courseProductId).orElseThrow()

        val courseUserProduct = CourseUserProduct(
            socUser = user,
            courseProduct = courseProduct,
            boughtAt = OffsetDateTime.now(),
            promoCode = promoCode.ifEmpty { null }
        )

        return courseUserProductRepository.save(courseUserProduct)
    }
}
