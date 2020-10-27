package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.model.Chapter

interface ChapterService {
    fun get(id: Long): Chapter
    fun getAll(): List<Chapter>
    fun getByCourseId(courseId: Long): List<Chapter>
    fun add(addRequest: ChapterAddRequest): Long
    fun delete(id: Long)
}