package sk.streetofcode.webapi.model

import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
data class NewsletterRegistration(
    @Id
    @SequenceGenerator(name = "newsletter_registration_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsletter_registration_id_seq")
    val id: Long? = null,

    @Column(nullable = true)
    val socUserFirebaseId: String? = null,

    @Column(nullable = false)
    var subscribedFrom: String,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = true)
    val fromPath: String? = null
) {
    constructor(
        firebaseId: String?,
        subscribedFrom: String,
        fromPath: String? = null
    ) : this(null, firebaseId, subscribedFrom, OffsetDateTime.now(), fromPath)
}
