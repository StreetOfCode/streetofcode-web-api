package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.DifficultyService
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest
import sk.streetofcode.courseplatformbackend.model.Difficulty

@RestController
@RequestMapping("difficulty")
class DifficultyController(val difficultyService: DifficultyService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<Difficulty>> {
        val difficulties = difficultyService.getAll()

        val httpHeaders = HttpHeaders()
        httpHeaders.add(
                "Content-Range",
                "difficulty 0-${difficulties.size}/${difficulties.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(difficulties)
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.get(id))
    }

    @PostMapping
    fun add(@RequestBody difficultyAddRequest: DifficultyAddRequest): ResponseEntity<Difficulty> {
        return ResponseEntity.status(HttpStatus.CREATED).body(difficultyService.add(difficultyAddRequest))
    }

    @PutMapping("{id}")
    fun edit(@PathVariable("id") id: Long, @RequestBody difficultyEditRequest: DifficultyEditRequest): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.edit(id, difficultyEditRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Void> {
        difficultyService.delete(id)
        return ResponseEntity.ok().build()
    }
}