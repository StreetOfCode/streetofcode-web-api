package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.model.Author

@RestController
@RequestMapping("author")
class AuthorController(val authorService: AuthorService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<Author>> {
        return ResponseEntity.ok(authorService.getAll())
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Author> {
        return ResponseEntity.ok(authorService.get(id))
    }

    @PostMapping
    fun add(@RequestBody authorAddRequest: AuthorAddRequest): ResponseEntity<Long> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.add(authorAddRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Void> {
        authorService.delete(id)
        return ResponseEntity.ok().build()
    }
}