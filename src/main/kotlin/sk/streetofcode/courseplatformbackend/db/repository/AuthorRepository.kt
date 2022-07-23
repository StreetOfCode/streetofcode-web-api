package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.Author
import java.util.Optional

@Repository
interface AuthorRepository : CrudRepository<Author, Long> {
    fun findBySlug(slug: String): Optional<Author>
}
