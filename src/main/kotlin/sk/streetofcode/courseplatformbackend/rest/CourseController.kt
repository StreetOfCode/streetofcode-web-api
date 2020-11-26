package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseHomepageDto
import sk.streetofcode.courseplatformbackend.api.dto.CourseOverviewDto
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.api.request.CourseEditRequest
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@RestController
@RequestMapping("course")
class CourseController(val courseService: CourseService, val authenticationService: AuthenticationService) {

    @GetMapping
    @PreAuthorize("hasRole('${AuthenticationService.ADMIN_GROUP_NAME}')")
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
    fun get(@PathVariable("id") id: Long): ResponseEntity<CourseDto> {
        return ResponseEntity.ok(courseService.get(id))
    }

    @PostMapping
    fun add(@RequestBody courseAddRequest: CourseAddRequest): ResponseEntity<CourseDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.add(courseAddRequest))
    }

    @PutMapping("{id}")
    fun edit(@PathVariable("id") id: Long, @RequestBody courseEditRequest: CourseEditRequest): ResponseEntity<CourseDto> {
        return ResponseEntity.ok(courseService.edit(id, courseEditRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<CourseDto> {
        return ResponseEntity.ok(courseService.delete(id))
    }

    @GetMapping("/home-page")
    fun getCoursesHomepage(): ResponseEntity<List<CourseHomepageDto>> {
        return ResponseEntity.ok(courseService.getCoursesHomepage())
    }

    @GetMapping("/overview/{id}")
    fun getCourseOverview(@PathVariable("id") id: Long): ResponseEntity<CourseOverviewDto> {
        return ResponseEntity.ok(courseService.getCourseOverview(id))
    }
}
