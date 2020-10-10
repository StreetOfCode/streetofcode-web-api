package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.courseplatformbackend.api.CourseService
import sk.streetofcode.courseplatformbackend.api.request.CourseAddRequest
import sk.streetofcode.courseplatformbackend.db.projection.CourseOverview
import sk.streetofcode.courseplatformbackend.db.projection.CoursesHomepageOverview
import sk.streetofcode.courseplatformbackend.model.Course

@RestController
@RequestMapping("course")
class CourseController(val courseService: CourseService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<Course>> {
        return ResponseEntity.ok(courseService.getAll())
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Course> {
        return ResponseEntity.ok(courseService.get(id))
    }

    @PostMapping
    fun add(@RequestBody courseAddRequest: CourseAddRequest): ResponseEntity<Long> {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.add(courseAddRequest))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Void> {
        courseService.delete(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/home-page")
    fun getCoursesHomepage(): ResponseEntity<List<CoursesHomepageOverview>> {
        return ResponseEntity.ok(courseService.getCoursesHomepage())
    }

    @GetMapping("/overview/{id}")
    fun getCourseOverview(@PathVariable("id") id: Long): ResponseEntity<CourseOverview> {
        return ResponseEntity.ok(courseService.getCourseOverview(id))
    }
}