package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.LectureChapterDto
import sk.streetofcode.courseplatformbackend.api.dto.LectureCommentDto
import sk.streetofcode.courseplatformbackend.api.dto.LectureCourseDto
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.model.Lecture
import sk.streetofcode.courseplatformbackend.model.LectureComment
import java.time.temporal.ChronoUnit

@Component
class LectureMapper() {
    fun toLectureDto(lecture: Lecture): LectureDto {
        return LectureDto(
            lecture.id!!,
            LectureChapterDto(lecture.chapter.id!!, lecture.chapter.name),
            LectureCourseDto(lecture.chapter.course.id!!, lecture.chapter.course.lecturesCount),
            lecture.name,
            lecture.lectureOrder,
            lecture.content,
            lecture.videoUrl,
            lecture.videoDurationSeconds,
            lecture.createdAt.truncatedTo(ChronoUnit.SECONDS),
            lecture.updatedAt.truncatedTo(ChronoUnit.SECONDS)
        )
    }

    fun toLectureCommentDto(lectureComment: LectureComment): LectureCommentDto {
        return LectureCommentDto(
            id = lectureComment.id!!,
            userId = lectureComment.userId,
            userName = lectureComment.userName,
            commentText = lectureComment.commentText,
            updatedAt = lectureComment.updatedAt
        )
    }
}
