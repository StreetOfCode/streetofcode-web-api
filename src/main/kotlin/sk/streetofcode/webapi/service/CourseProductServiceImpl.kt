package sk.streetofcode.webapi.service

import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.UserProductService
import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.api.dto.IsOwnedByUserDto
import sk.streetofcode.webapi.client.stripe.StripeApiClient
import sk.streetofcode.webapi.db.repository.CourseProductRepository
import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.toCourseProductDto

@Service
class CourseProductServiceImpl(
    val courseProductRepository: CourseProductRepository,
    val userProductService: UserProductService,
    val stripeApiClient: StripeApiClient
) : CourseProductService {
    override fun getAllForCourse(userId: String?, courseId: Long): List<CourseProductDto> {
        val courseProducts = courseProductRepository.findAllByCourseId(courseId)

        return courseProducts.map {
            val userProducts =
                if (userId != null) userProductService.getProductUserProducts(userId, it) else listOf()

            val price = stripeApiClient.getProductPrice(it.productId)

            it.toCourseProductDto(userProducts, price)
        }
    }

    override fun get(courseProductId: String): CourseProduct {
        return courseProductRepository.findById(courseProductId).orElseThrow()
    }

    override fun isOwnedByUser(userId: String, courseId: Long): IsOwnedByUserDto {
        return IsOwnedByUserDto(getAllForCourse(userId, courseId).any { it.userProducts.isNotEmpty() })
    }
}
