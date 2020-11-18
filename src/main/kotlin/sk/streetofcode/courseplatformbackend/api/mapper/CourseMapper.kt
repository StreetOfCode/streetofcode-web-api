package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.*
import sk.streetofcode.courseplatformbackend.model.Chapter
import sk.streetofcode.courseplatformbackend.model.Course

@Component
class CourseMapper() {
    fun toCourseDto(course: Course): CourseDto {
        return CourseDto(
                course.id!!,
                course.author,
                course.difficulty,
                course.name,
                course.shortDescription,
                course.longDescription,
                course.imageUrl,
                course.chapters.map { chapter -> toCourseChapterDto(chapter) }.toSet(),
                course.createdAt,
                course.updatedAt
        )
    }

    fun toCourseHomepage(course: Course): CourseHomepageDto {
        return CourseHomepageDto(
                course.id!!,
                course.name,
                course.shortDescription,
                course.author,
                course.difficulty
        )
    }

    fun toCourseOverview(course: Course): CourseOverviewDto {
        return CourseOverviewDto(
                course.id!!,
                course.name,
                course.shortDescription,
                course.longDescription,
                course.imageUrl,
                course.author,
                course.difficulty,
                course.createdAt,
                course.updatedAt,
                course.chapters.map { chapter ->
                    ChapterOverviewDto(
                            chapter.id!!,
                            chapter.name,
                            chapter.lectures.map { lecture ->
                                LectureOverviewDto(
                                        lecture.id!!,
                                        lecture.name
                                )
                            }
                    )
                }.toSet()
        )
    }

    private fun toCourseChapterDto(chapter: Chapter): CourseChapterDto {
        return CourseChapterDto(
                chapter.id!!,
                chapter.name
        )
    }
}