package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.PostComment

@Repository
interface PostCommentRepository : CrudRepository<PostComment, Long> {
    fun findAllByPostId(postId: String): List<PostComment>
}
