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

        @Column(nullable = false)
        val difficultyOrder: Int,

        @OneToMany(
                mappedBy = "difficulty",
                cascade = [CascadeType.PERSIST],
                fetch = FetchType.LAZY
        )
        @OrderBy("id")
        @JsonIgnore
        val courses: MutableSet<Course>
) {
        constructor(name: String, description: String, difficultyOrder: Int)
                : this(null, name, description, difficultyOrder, mutableSetOf())

        constructor(id: Long, name: String, description: String, difficultyOrder: Int)
                : this(id, name, description, difficultyOrder, mutableSetOf())

        override fun equals(other: Any?) = other is Difficulty && DifficultyEssential(this) == DifficultyEssential(other)
        override fun hashCode() = DifficultyEssential(this).hashCode()
        override fun toString() = DifficultyEssential(this).toString().replaceFirst("DifficultyEssential", "Difficulty")
}

private data class DifficultyEssential(
        val name: String,
        val description: String,
        val difficultyOrder: Int
) {
        constructor(difficulty: Difficulty) : this(
                name = difficulty.name,
                description = difficulty.description,
                difficultyOrder = difficulty.difficultyOrder
        )
}