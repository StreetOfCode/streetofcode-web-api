package sk.streetofcode.courseplatformbackend.model.vote

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Type
import java.util.UUID
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
    @Type(type = "uuid-char")
    val userId: UUID? = null,

    @Column(nullable = false)
    var nextCourseOptionId: Long
) {
    constructor(userId: UUID?, nextCourseOptionId: Long) : this(null, userId, nextCourseOptionId)

    override fun equals(other: Any?) =
        other is NextCourseVote && NextCourseVoteEssential(this) == NextCourseVoteEssential(
            other
        )

    override fun hashCode() = NextCourseVoteEssential(this).hashCode()
    override fun toString() =
        NextCourseVoteEssential(this).toString().replaceFirst("NextCourseVoteEssential", "NextCourseVote")
}

private data class NextCourseVoteEssential(
    val userId: UUID?,
    val nextCourseOptionId: Long
) {
    constructor(nextCourseVote: NextCourseVote) : this(
        userId = nextCourseVote.userId,
        nextCourseOptionId = nextCourseVote.nextCourseOptionId
    )
}
