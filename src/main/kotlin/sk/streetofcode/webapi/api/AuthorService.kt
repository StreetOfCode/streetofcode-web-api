package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.AuthorOverviewDto
import sk.streetofcode.webapi.api.request.AuthorAddRequest
import sk.streetofcode.webapi.api.request.AuthorEditRequest
import sk.streetofcode.webapi.model.Author

interface AuthorService {
    fun get(id: Long): Author
    fun getOverview(slug: String): AuthorOverviewDto
    fun getAll(): List<Author>
    fun add(addRequest: AuthorAddRequest): Author
    fun edit(id: Long, editRequest: AuthorEditRequest): Author
    fun delete(id: Long): Author
}
