package sk.streetofcode.webapi.model

import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.client.stripe.StripeProductWithPrice
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class CourseProduct(
    @Id
    @Column(nullable = false)
    val productId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    val course: Course,
)

fun CourseProduct.toCourseProductDto(userProducts: List<UserProduct>, price: Long): CourseProductDto =
    CourseProductDto(this.productId, this.course.id!!, userProducts.map { it.toUserProductDto() }, price)
