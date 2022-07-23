package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.AuthorOverviewDto
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.api.request.AuthorEditRequest
import sk.streetofcode.courseplatformbackend.model.Author

interface AuthorService {
    fun get(id: Long): Author
    fun getOverview(slug: String): AuthorOverviewDto
    fun getAll(): List<Author>
    fun add(addRequest: AuthorAddRequest): Author
    fun edit(id: Long, editRequest: AuthorEditRequest): Author
    fun delete(id: Long): Author
}
