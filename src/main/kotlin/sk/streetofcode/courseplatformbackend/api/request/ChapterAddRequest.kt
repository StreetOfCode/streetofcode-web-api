package sk.streetofcode.courseplatformbackend.api.request

import sk.streetofcode.courseplatformbackend.model.Course

data class ChapterAddRequest(
        val courseId: Long,
        val name: String,
        val chapterOrder: Int
)