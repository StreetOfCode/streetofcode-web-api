package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.model.Lecture

interface LectureService {
    fun get(id: Long): Lecture
    fun getAll(): List<Lecture>
    fun add(addRequest: LectureAddRequest): Long
    fun delete(id: Long)
}