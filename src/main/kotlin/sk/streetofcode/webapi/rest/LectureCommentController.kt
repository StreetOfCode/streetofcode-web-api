package sk.streetofcode.webapi.rest

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
import sk.streetofcode.webapi.api.LectureCommentService
import sk.streetofcode.webapi.api.dto.LectureCommentDto
import sk.streetofcode.webapi.api.request.LectureCommentAddRequest
import sk.streetofcode.webapi.api.request.LectureCommentEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("lecture/{lectureId}/comment")
class LectureCommentController(
    private val lectureCommentService: LectureCommentService,
    private val authenticationService: AuthenticationService
) {

    @GetMapping
    @IsAuthenticated
    fun getAll(@PathVariable("lectureId") lectureId: Long): ResponseEntity<List<LectureCommentDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(lectureCommentService.getAll(lectureId))
    }

    @PostMapping
    @IsAuthenticated
    fun add(
        @PathVariable("lectureId") lectureId: Long,
        @RequestBody lectureCommentAddRequest: LectureCommentAddRequest
    ): ResponseEntity<LectureCommentDto> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(lectureCommentService.add(authenticationService.getUserId(), lectureId, lectureCommentAddRequest))
    }

    @PutMapping("{commentId}")
    @IsAuthenticated
    fun edit(
        @PathVariable("lectureId") lectureId: Long,
        @PathVariable("commentId") commentId: Long,
        @RequestBody lectureCommentEditRequest: LectureCommentEditRequest
    ): ResponseEntity<LectureCommentDto> {
        return ResponseEntity.status(HttpStatus.OK).body(
            lectureCommentService.edit(
                authenticationService.getUserId(),
                lectureId,
                commentId,
                lectureCommentEditRequest
            )
        )
    }

    @DeleteMapping("{commentId}")
    @IsAuthenticated
    fun delete(
        @PathVariable("lectureId") lectureId: Long,
        @PathVariable("commentId") commentId: Long
    ): ResponseEntity<Void> {
        lectureCommentService.delete(authenticationService.getUserId(), lectureId, commentId)
        return ResponseEntity.ok().build()
    }
}
