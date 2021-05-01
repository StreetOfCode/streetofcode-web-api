package sk.streetofcode.courseplatformbackend.api.mapper

import org.springframework.stereotype.Component
import sk.streetofcode.courseplatformbackend.api.dto.*
import sk.streetofcode.courseplatformbackend.model.Chapter
import sk.streetofcode.courseplatformbackend.model.Course
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

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
            course.status,
            course.chapters.map { chapter -> toCourseChapterDto(chapter) }.toSet(),
            course.createdAt.truncatedTo(ChronoUnit.SECONDS),
            course.updatedAt.truncatedTo(ChronoUnit.SECONDS),
            course.lecturesCount
        )
    }

    fun toCourseHomepage(course: Course, courseReviewsOverview: CourseReviewsOverviewDto): CourseHomepageDto {
        return CourseHomepageDto(
            course.id!!,
            course.name,
            course.shortDescription,
            course.author,
            course.difficulty,
            course.imageUrl,
            courseReviewsOverview
        )
    }

    fun toCourseOverview(course: Course, courseReviewsOverview: CourseReviewsOverviewDto): CourseOverviewDto {

        val chapters = course.chapters.map { chapter ->
            ChapterOverviewDto(
                chapter.id!!,
                chapter.name,
                chapter.lectures.map { lecture ->
                    LectureOverviewDto(
                        lecture.id!!,
                        lecture.name,
                        lecture.videoDurationSeconds
                    )
                },
                chapterDurationMinutes = chapter.lectures.sumBy { lecture -> TimeUnit.SECONDS.toMinutes(lecture.videoDurationSeconds.toLong()).toInt() }
            )
        }.toSet()

        return CourseOverviewDto(
            course.id!!,
            course.name,
            course.shortDescription,
            course.longDescription,
            course.imageUrl,
            course.status,
            course.author,
            course.difficulty,
            course.createdAt.truncatedTo(ChronoUnit.SECONDS),
            course.updatedAt.truncatedTo(ChronoUnit.SECONDS),
            chapters,
            courseDurationMinutes = chapters.sumBy { chapter -> chapter.chapterDurationMinutes },
            reviewsOverview = courseReviewsOverview
        )
    }

    private fun toCourseChapterDto(chapter: Chapter): CourseChapterDto {
        return CourseChapterDto(
            chapter.id!!,
            chapter.name
        )
    }
}
