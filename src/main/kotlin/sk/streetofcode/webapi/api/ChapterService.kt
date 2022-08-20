package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.ChapterDto
import sk.streetofcode.webapi.api.request.ChapterAddRequest
import sk.streetofcode.webapi.api.request.ChapterEditRequest

interface ChapterService {
    fun get(id: Long): ChapterDto
    fun getAll(): List<ChapterDto>
    fun getByCourseId(courseId: Long): List<ChapterDto>
    fun add(addRequest: ChapterAddRequest): ChapterDto
    fun edit(id: Long, editRequest: ChapterEditRequest): ChapterDto
    fun delete(id: Long): ChapterDto
}
