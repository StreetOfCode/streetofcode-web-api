package sk.streetofcode.webapi.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.webapi.api.dto.PostCommentDto
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class PostComment(
    @Id
    @SequenceGenerator(name = "post_comment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_comment_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "soc_user_firebase_id", nullable = true)
    val socUser: SocUser? = null,

    @Column(nullable = false)
    val postId: String,

    @Column(nullable = false)
    val postSlug: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var commentText: String,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var updatedAt: OffsetDateTime
) {
    constructor(socUser: SocUser? = null, postId: String, postSlug: String, commentText: String) :
            this(null, socUser, postId, postSlug, commentText, OffsetDateTime.now(), OffsetDateTime.now())

    override fun equals(other: Any?) =
        other is PostComment && PostCommentEssential(this) == PostCommentEssential(other)

    override fun hashCode() = PostCommentEssential(this).hashCode()
    override fun toString() = PostCommentEssential(this).toString().replaceFirst("PostEssential", "Post")
}

private data class PostCommentEssential(
    val userId: String? = null,
    val commentText: String,
    val postId: String,
    val postSlug: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime

) {
    constructor(postComment: PostComment) : this(
        userId = postComment.socUser?.firebaseId,
        commentText = postComment.commentText,
        postId = postComment.postId,
        postSlug = postComment.postSlug,
        createdAt = postComment.createdAt,
        updatedAt = postComment.updatedAt
    )
}

fun PostComment.toPostCommentDto(): PostCommentDto {
    return PostCommentDto(
        id = this.id!!,
        postId = this.postId,
        postSlug = this.postSlug,
        userId = this.socUser?.firebaseId,
        userName = this.socUser?.name,
        imageUrl = this.socUser?.imageUrl,
        commentText = this.commentText,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
