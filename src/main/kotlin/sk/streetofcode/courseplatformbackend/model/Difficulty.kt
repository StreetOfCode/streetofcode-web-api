package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Difficulty(
        @Id
        @SequenceGenerator(name = "difficulty_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "difficulty_id_seq")
        val id: Long? = null,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val description: String,

        @OneToMany(
                mappedBy = "difficulty",
                cascade = [CascadeType.PERSIST],
                fetch = FetchType.LAZY
        )
        @OrderBy("id")
        @JsonIgnore
        val courses: MutableSet<Course> = mutableSetOf()
) {
        constructor(name: String, description: String)
                : this(null, name, description, mutableSetOf())

        constructor(id: Long, name: String, description: String)
                : this(id, name, description, mutableSetOf())

        override fun equals(other: Any?) = other is Difficulty && DifficultyEssential(this) == DifficultyEssential(other)
        override fun hashCode() = DifficultyEssential(this).hashCode()
        override fun toString() = DifficultyEssential(this).toString().replaceFirst("DifficultyEssential", "Difficulty")
}

private data class DifficultyEssential(
        val name: String,
        val description: String
) {
        constructor(difficulty: Difficulty) : this(
                name = difficulty.name,
                description = difficulty.description
        )
}