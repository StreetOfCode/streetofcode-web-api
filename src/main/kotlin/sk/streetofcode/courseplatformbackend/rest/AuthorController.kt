package sk.streetofcode.courseplatformbackend.rest

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
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.dto.AuthorOverviewDto
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

    @GetMapping("{id}/overview")
    fun getOverview(@PathVariable("id") id: Long): ResponseEntity<AuthorOverviewDto> {
        return ResponseEntity.ok(authorService.getOverview(id))
    }

    @GetMapping("id")
    fun getAllIds(): ResponseEntity<List<Long>> {
        return ResponseEntity.ok(authorService.getAll().map { it.id!! }.toList())
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
