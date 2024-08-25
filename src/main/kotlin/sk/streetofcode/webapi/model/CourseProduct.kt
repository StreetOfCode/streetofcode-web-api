package sk.streetofcode.webapi.model

import sk.streetofcode.webapi.api.dto.CourseProductDto
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

    @Column(nullable = true)
    val archived: Boolean = false

)

fun CourseProduct.toCourseProductDto(courseUserProducts: List<CourseUserProduct>, price: Long?): CourseProductDto =
    CourseProductDto(this.productId, this.course.id!!, courseUserProducts.map { it.toUserProductDto() }, price, this.archived)
