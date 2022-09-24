package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.LectureDto
import sk.streetofcode.webapi.api.request.LectureAddRequest
import sk.streetofcode.webapi.api.request.LectureEditRequest

interface LectureService {
    fun get(id: Long): LectureDto
    fun getAll(order: LectureOrderSort): List<LectureDto>
    fun getByChapterId(chapterId: Long, order: LectureOrderSort): List<LectureDto>
    fun add(addRequest: LectureAddRequest): LectureDto
    fun edit(id: Long, editRequest: LectureEditRequest): LectureDto
    fun delete(id: Long): LectureDto
}

enum class LectureOrderSort {
    ASC, DESC
}
