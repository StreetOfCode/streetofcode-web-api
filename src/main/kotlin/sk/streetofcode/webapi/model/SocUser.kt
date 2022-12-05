package sk.streetofcode.webapi.model

import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class SocUser(
    @Id
    @Column(nullable = false)
    val firebaseId: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = true)
    val imageUrl: String? = null,

    @Column(nullable = false)
    val receiveNewsletter: Boolean,

    @Column(nullable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime? = null,
) {
    constructor(
        firebaseId: String,
        name: String,
        email: String,
        imageUrl: String?,
        receiveNewsletter: Boolean
    ) : this(firebaseId, name, email, imageUrl, receiveNewsletter, OffsetDateTime.now())
}
