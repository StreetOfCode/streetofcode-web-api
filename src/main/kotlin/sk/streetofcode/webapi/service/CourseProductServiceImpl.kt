package sk.streetofcode.webapi.service

import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.CourseUserProductService
import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.api.dto.IsOwnedByUserDto
import sk.streetofcode.webapi.client.stripe.StripeApiClient
import sk.streetofcode.webapi.db.repository.CourseProductRepository
import sk.streetofcode.webapi.model.CourseProduct
import sk.streetofcode.webapi.model.toCourseProductDto

@Service
class CourseProductServiceImpl(
    val courseProductRepository: CourseProductRepository,
    val courseUserProductService: CourseUserProductService,
    val stripeApiClient: StripeApiClient,
    val authenticationService: AuthenticationService
) : CourseProductService {
    override fun getAllForCourse(courseId: Long): List<CourseProductDto> {
        val userId = authenticationService.getNullableUserId()
        val courseProducts = courseProductRepository.findAllByCourseId(courseId)

        return courseProducts.map {
            val courseUserProducts =
                if (userId != null) courseUserProductService.getProductCourseUserProducts(userId, it) else listOf()

            val price = stripeApiClient.getProductPrice(it.productId)

            it.toCourseProductDto(courseUserProducts, price)
        }
    }

    override fun get(courseProductId: String): CourseProduct {
        return courseProductRepository.findById(courseProductId).orElseThrow()
    }

    override fun isOwnedByUser(courseId: Long): IsOwnedByUserDto {
        val courseProducts = getAllForCourse(courseId)

        val hasProducts = courseProducts.isNotEmpty()
        val isOwnedByUser = if (!hasProducts) {
            true
        } else {
            val hasCourseUserProducts = courseProducts.any { it.courseUserProducts.isNotEmpty() }
            val isAdmin = authenticationService.isAdmin()

            hasCourseUserProducts || isAdmin
        }

        return IsOwnedByUserDto(isOwnedByUser)
    }
}
