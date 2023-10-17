package sk.streetofcode.webapi.model

import sk.streetofcode.webapi.api.dto.CourseUserProductDto
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["soc_user_firebase_id", "product_id"])])
data class CourseUserProduct(
    @Id
    @SequenceGenerator(name = "course_user_product_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_user_product_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "soc_user_firebase_id", nullable = false)
    val socUser: SocUser,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val courseProduct: CourseProduct,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val boughtAt: OffsetDateTime,

    @Column(nullable = true)
    val promoCode: String?
) {
    constructor(socUser: SocUser, courseProduct: CourseProduct, boughtAt: OffsetDateTime, promoCode: String?) :
        this(null, socUser, courseProduct, boughtAt, promoCode)
}

fun CourseUserProduct.toUserProductDto() =
    CourseUserProductDto(this.boughtAt)
