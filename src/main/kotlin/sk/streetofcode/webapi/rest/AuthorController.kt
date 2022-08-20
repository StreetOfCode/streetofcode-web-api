package sk.streetofcode.webapi.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.AuthorService
import sk.streetofcode.webapi.api.dto.AuthorOverviewDto
import sk.streetofcode.webapi.api.request.AuthorAddRequest
import sk.streetofcode.webapi.api.request.AuthorEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAdmin
import sk.streetofcode.webapi.model.Author

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

    @GetMapping("{slug}/overview")
    fun getOverview(@PathVariable("slug") slug: String): ResponseEntity<AuthorOverviewDto> {
        return ResponseEntity.ok(authorService.getOverview(slug))
    }

    @GetMapping("slug")
    fun getAllSlugs(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(authorService.getAll().map { it.slug }.toList())
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
