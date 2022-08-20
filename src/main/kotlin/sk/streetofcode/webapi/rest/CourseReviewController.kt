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
import sk.streetofcode.webapi.api.CourseReviewService
import sk.streetofcode.webapi.api.dto.CourseReviewDto
import sk.streetofcode.webapi.api.dto.CourseReviewsOverviewDto
import sk.streetofcode.webapi.api.request.CourseReviewAddRequest
import sk.streetofcode.webapi.api.request.CourseReviewEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("course-review")
class CourseReviewController(private val courseReviewService: CourseReviewService, private val authenticationService: AuthenticationService) {
    @GetMapping("/course/{courseId}")
    fun getCourseReviews(@PathVariable("courseId") courseId: Long): ResponseEntity<List<CourseReviewDto>> {
        return ResponseEntity.ok(courseReviewService.getCourseReviews(courseId))
    }

    @GetMapping("/course/{courseId}/overview")
    fun getOverview(@PathVariable("courseId") courseId: Long): ResponseEntity<CourseReviewsOverviewDto> {
        return ResponseEntity.ok(courseReviewService.getCourseReviewsOverview(courseId))
    }

    @GetMapping("/{id}")
    @IsAuthenticated
    fun get(@PathVariable("id") id: Long): ResponseEntity<CourseReviewDto> {
        return ResponseEntity.ok(courseReviewService.get(id))
    }

    @PostMapping
    @IsAuthenticated
    fun add(@RequestBody courseReviewAddRequest: CourseReviewAddRequest): ResponseEntity<CourseReviewDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseReviewService.add(courseReviewAddRequest))
    }

    @PutMapping("/{id}")
    @IsAuthenticated
    fun edit(@PathVariable("id") id: Long, @RequestBody courseReviewEditRequest: CourseReviewEditRequest): ResponseEntity<CourseReviewDto> {
        return ResponseEntity.ok(courseReviewService.edit(id, courseReviewEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAuthenticated
    fun delete(@PathVariable("id") id: Long): ResponseEntity<CourseReviewDto> {
        return ResponseEntity.ok(courseReviewService.delete(id))
    }
}
