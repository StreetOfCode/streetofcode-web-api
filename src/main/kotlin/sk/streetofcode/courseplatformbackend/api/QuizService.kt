package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizDto
import sk.streetofcode.courseplatformbackend.api.request.QuizAddRequest
import sk.streetofcode.courseplatformbackend.api.request.QuizEditRequest

interface QuizService {
    fun get(id: Long): QuizDto
    fun getAll(): List<QuizDto>
    fun getAllForLecture(lectureId: Long): List<QuizDto>
    fun add(addRequest: QuizAddRequest): QuizDto
    fun edit(id: Long, editRequest: QuizEditRequest): QuizDto
    fun delete(id: Long): QuizDto
}
