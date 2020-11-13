package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.ChapterDto
import sk.streetofcode.courseplatformbackend.api.request.ChapterAddRequest
import sk.streetofcode.courseplatformbackend.api.request.ChapterEditRequest

interface ChapterService {
    fun get(id: Long): ChapterDto
    fun getAll(): List<ChapterDto>
    fun getByCourseId(courseId: Long): List<ChapterDto>
    fun add(addRequest: ChapterAddRequest): ChapterDto
    fun edit(id: Long, editRequest: ChapterEditRequest): ChapterDto
    fun delete(id: Long): ChapterDto
}