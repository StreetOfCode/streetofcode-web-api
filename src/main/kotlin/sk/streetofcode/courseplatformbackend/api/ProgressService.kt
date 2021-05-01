package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.courseplatformbackend.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.courseplatformbackend.api.request.ResetProgressDto
import java.util.UUID

interface ProgressService {
    fun updateProgress(userId: UUID, lectureId: Long)
    fun resetProgress(userId: UUID, resetProgressDto: ResetProgressDto)
    fun getProgressOverview(userId: UUID, courseId: Long): CourseProgressOverviewDto
    fun getUserProgressMetadata(userId: UUID, courseId: Long): UserProgressMetadataDto
}
