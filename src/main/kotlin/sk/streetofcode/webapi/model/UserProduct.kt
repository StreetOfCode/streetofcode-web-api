package sk.streetofcode.webapi.model

import sk.streetofcode.webapi.api.dto.UserProductDto
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

// TODO STRIPE - rename to UserCourseProduct
@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["soc_user_firebase_id", "product_id"])])
data class UserProduct(
    @Id
    @SequenceGenerator(name = "user_product_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_product_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "soc_user_firebase_id", nullable = false)
    val socUser: SocUser,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val courseProduct: CourseProduct,

    @Column(nullable = false)
    val priceId: String,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val boughtAt: OffsetDateTime,
) {
    constructor(socUser: SocUser, courseProduct: CourseProduct, priceId: String, boughtAt: OffsetDateTime) :
        this(null, socUser, courseProduct, priceId, boughtAt)
}

fun UserProduct.toUserProductDto() =
    UserProductDto(this.priceId, this.boughtAt)
