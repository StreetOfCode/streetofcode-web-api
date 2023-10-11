package sk.streetofcode.webapi.service

import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.SocUserService
import sk.streetofcode.webapi.api.UserProductService
import sk.streetofcode.webapi.db.repository.CourseProductRepository
import sk.streetofcode.webapi.db.repository.UserProductRepository
import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.UserProduct
import java.time.OffsetDateTime

@Service
class UserProductServiceImpl(
    val userProductRepository: UserProductRepository,
    val courseProductRepository: CourseProductRepository,
    val userService: SocUserService
) : UserProductService {
    override fun getProductUserProducts(userId: String, courseProduct: CourseProduct): List<UserProduct> =
        userProductRepository.findBySocUserFirebaseIdAndCourseProduct(userId, courseProduct)

    override fun addUserProduct(userId: String, courseProductId: String, priceId: String): UserProduct {
        val user = userService.get(userId)
        val courseProduct = courseProductRepository.findById(courseProductId).orElseThrow()

        val userProduct = UserProduct(
            socUser = user,
            courseProduct = courseProduct,
            priceId = priceId,
            boughtAt = OffsetDateTime.now()
        )

        return userProductRepository.save(userProduct)
    }
}
