package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.api.request.ResetProgressDto

interface ProgressService {
    fun updateProgress(userId: String, lectureId: Long): CourseProgressOverviewDto
    fun resetProgress(userId: String, resetProgressDto: ResetProgressDto): CourseProgressOverviewDto
    fun getProgressOverview(userId: String, courseId: Long): CourseProgressOverviewDto
    fun getUserProgressMetadata(userId: String, courseId: Long): UserProgressMetadataDto
    fun getUserProgressMetadataOrNull(userId: String, courseId: Long): UserProgressMetadataDto?
    fun getStartedCourseIds(userId: String): List<Long>
}
