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
}