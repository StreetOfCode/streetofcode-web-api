package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.DifficultyService
import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
import sk.streetofcode.courseplatformbackend.model.Difficulty

@RestController
@RequestMapping("difficulty")
class DifficultyController(val difficultyService: DifficultyService) {

    @GetMapping
    @IsAdmin
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
    @IsAdmin
    fun get(@PathVariable("id") id: Long): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.get(id))
    }

    @PostMapping
    @IsAdmin
    fun add(@RequestBody difficultyAddRequest: DifficultyAddRequest): ResponseEntity<Difficulty> {
        return ResponseEntity.status(HttpStatus.CREATED).body(difficultyService.add(difficultyAddRequest))
    }

    @PutMapping("{id}")
    @IsAdmin
    fun edit(@PathVariable("id") id: Long, @RequestBody difficultyEditRequest: DifficultyEditRequest): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.edit(id, difficultyEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.delete(id))
    }
}
