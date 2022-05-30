package sk.streetofcode.courseplatformbackend.model.vote

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class NextCourseVoteOption(
    @Id
    @SequenceGenerator(name = "next_course_vote_option_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "next_course_vote_option_id_seq")
    val id: Long? = null,

    @Column(nullable = false)
    var name: String
) {
    constructor(name: String) : this(null, name)

    override fun equals(other: Any?) =
        other is NextCourseVoteOption && NextCourseVoteOptionEssential(this) == NextCourseVoteOptionEssential(other)

    override fun hashCode() = NextCourseVoteOptionEssential(this).hashCode()
    override fun toString() =
        NextCourseVoteOptionEssential(this).toString().replaceFirst("NextCourseVoteOptionEssential", "NextCourseVoteOption")
}

private data class NextCourseVoteOptionEssential(
    val name: String
) {
    constructor(nextCourseOption: NextCourseVoteOption) : this(
        name = nextCourseOption.name
    )
}
