package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator

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
    val skillLevel: Int,

    @OneToMany(
        mappedBy = "difficulty",
        cascade = [CascadeType.PERSIST],
        fetch = FetchType.LAZY
    )
    @OrderBy("id")
    @JsonIgnore
    val courses: MutableSet<Course> = mutableSetOf()
) {
    constructor(name: String, skillLevel: Int) :
        this(null, name, skillLevel, mutableSetOf())

    constructor(id: Long, name: String, skillLevel: Int) :
        this(id, name, skillLevel, mutableSetOf())

    override fun equals(other: Any?) = other is Difficulty && DifficultyEssential(this) == DifficultyEssential(other)
    override fun hashCode() = DifficultyEssential(this).hashCode()
    override fun toString() = DifficultyEssential(this).toString().replaceFirst("DifficultyEssential", "Difficulty")
}

private data class DifficultyEssential(
    val name: String,
    val skillLevel: Int
) {
    constructor(difficulty: Difficulty) : this(
        name = difficulty.name,
        skillLevel = difficulty.skillLevel
    )
}
