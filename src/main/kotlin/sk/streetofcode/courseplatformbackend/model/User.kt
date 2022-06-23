package sk.streetofcode.courseplatformbackend.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
    @Id
    @Column(nullable = false)
    val firebaseId: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val imageUrl: String,
)
