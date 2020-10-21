package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.api.request.AuthorEditRequest
import sk.streetofcode.courseplatformbackend.model.Author

@RestController
@RequestMapping("author")
class AuthorController(val authorService: AuthorService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<Author>> {

        val authors = authorService.getAll()

        val httpHeaders = HttpHeaders()
        httpHeaders.add(
                "Content-Range",
                "author 0-${authors.size}/${authors.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(authors)
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Author> {
        return ResponseEntity.ok(authorService.get(id))
    }

    @PostMapping
    fun add(@RequestBody authorAddRequest: AuthorAddRequest): ResponseEntity<Author> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.add(authorAddRequest))
    }

    @PutMapping("{id}")
    fun edit(@PathVariable("id") id: Long, @RequestBody authorEditRequest: AuthorEditRequest): ResponseEntity<Author> {
        return ResponseEntity.ok(authorService.edit(id, authorEditRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Author> {
        return ResponseEntity.ok(authorService.delete(id))
    }
}