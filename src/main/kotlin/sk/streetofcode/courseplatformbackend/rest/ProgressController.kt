package sk.streetofcode.courseplatformbackend.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.courseplatformbackend.api.ProgressService
import sk.streetofcode.courseplatformbackend.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.courseplatformbackend.api.request.ResetProgressDto
import sk.streetofcode.courseplatformbackend.configuration.annotation.IsAuthenticated
import sk.streetofcode.courseplatformbackend.service.AuthenticationService

@RestController
@RequestMapping("progress")
class ProgressController(private val authenticationService: AuthenticationService, private val progressService: ProgressService) {

    @PostMapping("/update/{lectureId}")
    @IsAuthenticated
    fun updateProgress(@PathVariable("lectureId") lectureId: Long): ResponseEntity<Void> {
        progressService.updateProgress(authenticationService.getUserId(), lectureId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/reset")
    @IsAuthenticated
    fun resetProgress(@RequestBody resetProgress: ResetProgressDto): ResponseEntity<Void> {
        progressService.resetProgress(authenticationService.getUserId(), resetProgress)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/overview/{courseId}")
    @IsAuthenticated
    fun getProgressOverview(@PathVariable("courseId") courseId: Long): ResponseEntity<CourseProgressOverviewDto> {
        return ResponseEntity.ok(progressService.getProgressOverview(authenticationService.getUserId(), courseId))
    }

    @GetMapping("/metadata/{courseId}")
    @IsAuthenticated
    fun getUserProgressMetadata(@PathVariable("courseId") courseId: Long): ResponseEntity<UserProgressMetadataDto> {
        return ResponseEntity.ok(progressService.getUserProgressMetadata(authenticationService.getUserId(), courseId))
    }
}
