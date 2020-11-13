package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.api.request.LectureEditRequest

interface LectureService {
    fun get(id: Long): LectureDto
    fun getAll(): List<LectureDto>
    fun getByChapterId(chapterId: Long): List<LectureDto>
    fun add(addRequest: LectureAddRequest): LectureDto
    fun edit(id: Long, editRequest: LectureEditRequest): LectureDto
    fun delete(id: Long): LectureDto
}