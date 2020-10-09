package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.model.Author

interface AuthorService {
    fun get(id: Long): Author
    fun getAll(): List<Author>
    fun add(addRequest: AuthorAddRequest): Long
    fun delete(id: Long)
}