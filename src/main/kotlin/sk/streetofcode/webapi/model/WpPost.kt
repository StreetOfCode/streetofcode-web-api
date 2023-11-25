package sk.streetofcode.webapi.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class WpPost(
    @Id
    @Column(nullable = false)
    val slug: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val post: String,
) {
    override fun equals(other: Any?) = other is WpPost && WpPostEssential(this) == WpPostEssential(other)
    override fun hashCode() = WpPostEssential(this).hashCode()

    override fun toString() = WpPostEssential(this).toString().replaceFirst("WpPostEssential", "WpPost")
}

private data class WpPostEssential(
    val slug: String,
) {
    constructor(wpPost: WpPost) : this(
        slug = wpPost.slug,
    )
}
