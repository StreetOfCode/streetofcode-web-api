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
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAuthenticated
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@RestController
@RequestMapping("course")
class CourseController(val courseService: CourseService, val authenticationService: AuthenticationService) {

    @GetMapping
    @IsAdmin
    fun getAll(): ResponseEntity<List<CourseDto>> {

        val courses = courseService.getAll()

        val httpHeaders = HttpHeaders()
        httpHeaders.add(
            "Content-Range",
            "course 0-${courses.size}/${courses.size}"
        )

        return ResponseEntity.ok().headers(httpHeaders).body(courses)
    }

    @GetMapping("id")
    fun getAllIds(): ResponseEntity<List<Long>> {
        return ResponseEntity.ok(courseService.getAll().map { it.id!! }.toList())
    }

    @GetMapping("{id}")
    @IsAdmin
    fun get(@PathVariable("id") id: Long): ResponseEntity<CourseDto> {
        return ResponseEntity.ok(courseService.get(id))
    }

    @PostMapping
    @IsAdmin
    fun add(@RequestBody courseAddRequest: CourseAddRequest): ResponseEntity<CourseDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.add(courseAddRequest))
    }

    @PutMapping("{id}")
    @IsAdmin
    fun edit(@PathVariable("id") id: Long, @RequestBody courseEditRequest: CourseEditRequest): ResponseEntity<CourseDto> {
        return ResponseEntity.ok(courseService.edit(id, courseEditRequest))
    }

    @DeleteMapping("{id}")
    @IsAdmin
    fun delete(@PathVariable("id") id: Long): ResponseEntity<CourseDto> {
        return ResponseEntity.ok(courseService.delete(id))
    }

    @GetMapping("/overview")
    fun getAllOverviews(): ResponseEntity<List<CourseOverviewDto>> {
        return if (authenticationService.isAdmin()) {
            ResponseEntity.ok(courseService.getAllCoursesOverview())
        } else {
            ResponseEntity.ok(courseService.getPublicCoursesOverview())
        }
    }

    @GetMapping("/overview/{id}")
    fun getOverview(@PathVariable("id") id: Long): ResponseEntity<CourseOverviewDto> {
        return if (authenticationService.isAdmin()) {
            ResponseEntity.ok(courseService.getAnyCourseOverview(authenticationService.getUserId(), id))
        } else if (authenticationService.isAuthenticated()) {
            ResponseEntity.ok(courseService.getPublicCourseOverview(authenticationService.getUserId(), id))
        } else {
            ResponseEntity.ok(courseService.getPublicCourseOverview(null, id))
        }
    }

    @GetMapping("/my-courses")
    @IsAuthenticated
    fun getMyCourses(): ResponseEntity<List<CourseOverviewDto>> {
        return ResponseEntity.ok(courseService.getMyCourses(authenticationService.getUserId()))
    }
}
