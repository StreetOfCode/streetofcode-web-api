package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.api.request.AuthorEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
import sk.streetofcode.courseplatformbackend.model.Author

@RestController
@RequestMapping("author")
class AuthorController(val authorService: AuthorService) {

    @GetMapping
    @IsAdmin
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
    @IsAdmin
    fun get(@PathVariable("id") id: Long): ResponseEntity<Author> {
        return ResponseEntity.ok(authorService.get(id))
    }

    @PostMapping
    @IsAdmin
    fun add(@RequestBody authorAddRequest: AuthorAddRequest): ResponseEntity<Author> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.add(authorAddRequest))
    }

    @PutMapping("{id}")
    @IsAdmin
    fun edit(@PathVariable("id") id: Long, @RequestBody authorEditRequest: AuthorEditRequest): ResponseEntity<Author> {
        return ResponseEntity.ok(authorService.edit(id, authorEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Author> {
        return ResponseEntity.ok(authorService.delete(id))
    }
}