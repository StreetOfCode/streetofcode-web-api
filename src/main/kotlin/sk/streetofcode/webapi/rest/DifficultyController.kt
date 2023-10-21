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
import sk.streetofcode.webapi.api.DifficultyService
import sk.streetofcode.webapi.api.request.DifficultyAddRequest
import sk.streetofcode.webapi.api.request.DifficultyEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAdmin
import sk.streetofcode.webapi.model.Difficulty

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
    fun edit(
        @PathVariable("id") id: Long,
        @RequestBody difficultyEditRequest: DifficultyEditRequest
    ): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.edit(id, difficultyEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Difficulty> {
        return ResponseEntity.ok(difficultyService.delete(id))
    }
}
