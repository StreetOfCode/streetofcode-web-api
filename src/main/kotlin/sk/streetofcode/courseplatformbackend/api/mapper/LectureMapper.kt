package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.LectureChapterDto
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.model.Lecture

@Component
class LectureMapper() {
    fun toLectureDto(lecture: Lecture): LectureDto {
        return LectureDto(
                lecture.id!!,
                LectureChapterDto(lecture.chapter.id!!, lecture.chapter.name),
                lecture.name,
                lecture.lectureOrder,
                lecture.content,
                lecture.createdAt,
                lecture.updatedAt
        )
    }
}