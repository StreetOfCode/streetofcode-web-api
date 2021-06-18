package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseHomepageDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseMyDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAdmin
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

    @GetMapping("/home-page")
    fun getCoursesHomepage(): ResponseEntity<List<CourseHomepageDto>> {
        return if (authenticationService.isAdmin()) {
            ResponseEntity.ok(courseService.getAllCoursesHomepage())
        } else {
            ResponseEntity.ok(courseService.getPublicCoursesHomepage())
        }
    }

    @GetMapping("/overview/{id}")
    fun getCourseOverview(@PathVariable("id") id: Long): ResponseEntity<CourseOverviewDto> {
        return if (authenticationService.isAdmin()) {
            ResponseEntity.ok(courseService.getAnyCourseOverview(authenticationService.getUserId(), id))
        } else if (authenticationService.isAuthenticated()) {
            ResponseEntity.ok(courseService.getPublicCourseOverview(authenticationService.getUserId(), id))
        } else {
            ResponseEntity.ok(courseService.getPublicCourseOverview(null, id))
        }
    }

    @GetMapping("/my-courses")
    fun getMyCourses(): ResponseEntity<List<CourseMyDto>> {
        return if (!authenticationService.isAuthenticated()) {
            ResponseEntity.ok(listOf())
        } else {
            ResponseEntity.ok(courseService.getMyCourses(authenticationService.getUserId()))
        }
    }
}
