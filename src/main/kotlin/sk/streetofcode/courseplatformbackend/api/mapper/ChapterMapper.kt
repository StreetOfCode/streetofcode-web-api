package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.ChapterCourseDto
import sk.streetofcode.courseplatformbackend.api.dto.ChapterDto
import sk.streetofcode.courseplatformbackend.model.Chapter
import java.time.temporal.ChronoUnit

@Component
class ChapterMapper(val lectureMapper: LectureMapper) {

    fun toChapterDto(chapter: Chapter): ChapterDto {
        return ChapterDto(
                chapter.id!!,
                ChapterCourseDto(chapter.course.id!!, chapter.course.name),
                chapter.name,
                chapter.chapterOrder,
                chapter.lectures.map { lecture -> lectureMapper.toLectureDto(lecture) }.toSet(),
                chapter.createdAt.truncatedTo(ChronoUnit.SECONDS),
                chapter.updatedAt.truncatedTo(ChronoUnit.SECONDS)
        )
    }
}

