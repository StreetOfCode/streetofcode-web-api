package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.WpPost

@Repository
interface WpPostRepository : CrudRepository<WpPost, String> {
    fun findBySlug(slug: String): WpPost?
}
