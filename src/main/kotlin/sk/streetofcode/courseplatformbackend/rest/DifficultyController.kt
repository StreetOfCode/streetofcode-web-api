package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.AuthorService
import sk.streetofcode.courseplatformbackend.api.DifficultyService
import sk.streetofcode.courseplatformbackend.api.request.AuthorAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Difficulty

@RestController
@RequestMapping("difficulty")
class DifficultyController(val difficultyService: DifficultyService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<Difficulty>> {
        return ResponseEntity.ok(difficultyService.getAll())
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.get(id))
    }

    @PostMapping
    fun add(@RequestBody difficultyAddRequest: DifficultyAddRequest): ResponseEntity<Long> {
        return ResponseEntity.status(HttpStatus.CREATED).body(difficultyService.add(difficultyAddRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Void> {
        difficultyService.delete(id)
        return ResponseEntity.ok().build()
    }
}