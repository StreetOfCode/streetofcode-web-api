package sk.streetofcode.webapi.model.vote

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class NextCourseVote(
    @Id
    @SequenceGenerator(name = "next_course_vote_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "next_course_vote_id_seq")
    val id: Long? = null,

    @Column(nullable = true)
    val userId: String? = null,

    @Column(nullable = false)
    var nextCourseOptionId: Long
) {
    constructor(userId: String?, nextCourseOptionId: Long) : this(null, userId, nextCourseOptionId)

    override fun equals(other: Any?) =
        other is NextCourseVote && NextCourseVoteEssential(this) == NextCourseVoteEssential(
            other
        )

    override fun hashCode() = NextCourseVoteEssential(this).hashCode()
    override fun toString() =
        NextCourseVoteEssential(this).toString().replaceFirst("NextCourseVoteEssential", "NextCourseVote")
}

private data class NextCourseVoteEssential(
    val userId: String?,
    val nextCourseOptionId: Long
) {
    constructor(nextCourseVote: NextCourseVote) : this(
        userId = nextCourseVote.userId,
        nextCourseOptionId = nextCourseVote.nextCourseOptionId
    )
}
