package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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
                cascade = [CascadeType.ALL],
                fetch = FetchType.LAZY
        )
        @JsonIgnore
        val courses: MutableSet<Course>
) {
        constructor(name: String, description: String, difficultyOrder: Int)
                : this(null, name, description, difficultyOrder, mutableSetOf())
}