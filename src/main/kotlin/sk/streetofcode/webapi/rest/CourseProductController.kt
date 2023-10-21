package sk.streetofcode.webapi.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.CourseProductService
import sk.streetofcode.webapi.api.dto.CourseProductDto
import sk.streetofcode.webapi.api.dto.IsOwnedByUserDto
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("course-product")
class CourseProductController(
    val courseProductService: CourseProductService,
    val authenticationService: AuthenticationService
) {
    @GetMapping
    fun getAllForCourse(@PathVariable("courseId") courseId: Long): ResponseEntity<List<CourseProductDto>> {
        return ResponseEntity.ok(courseProductService.getAllForCourse(courseId))
    }

    @IsAuthenticated
    @GetMapping("{courseId}/is-owned-by-user")
    fun getIsOwnedByUser(@PathVariable("courseId") courseId: Long): ResponseEntity<IsOwnedByUserDto> =
        ResponseEntity.ok(courseProductService.isOwnedByUser(courseId))
}
