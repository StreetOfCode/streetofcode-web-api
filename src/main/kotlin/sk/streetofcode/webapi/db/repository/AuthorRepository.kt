package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.Author
import java.util.Optional

@Repository
interface AuthorRepository : CrudRepository<Author, Long> {
    fun findBySlug(slug: String): Optional<Author>
}
