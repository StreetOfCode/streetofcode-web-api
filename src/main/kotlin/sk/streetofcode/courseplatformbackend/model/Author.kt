package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Author(
        @Id
        @SequenceGenerator(name = "author_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
        val id: Long? = null,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val url: String,

        @Column(nullable = false)
        val description: String,

        @OneToMany(
                mappedBy = "author",
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY
        )
        @JsonIgnore
        val courses: MutableSet<Course>
) {
    constructor(name: String, url: String, description: String)
            : this(null, name, url, description, mutableSetOf())

        override fun equals(other: Any?) = other is Author && AuthorEssential(this) == AuthorEssential(other)
        override fun hashCode() = AuthorEssential(this).hashCode()
        override fun toString() = AuthorEssential(this).toString().replaceFirst("AuthorEssential", "Author")
}

private data class AuthorEssential(
        val name: String,
        val url: String,
        val description: String
) {
        constructor(author: Author) : this(
                name = author.name,
                url = author.url,
                description = author.description
        )
}