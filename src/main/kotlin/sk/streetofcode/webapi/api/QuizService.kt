package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.quiz.QuizDto
import sk.streetofcode.webapi.api.request.QuizAddRequest
import sk.streetofcode.webapi.api.request.QuizEditRequest

interface QuizService {
    fun get(id: Long): QuizDto
    fun getAll(): List<QuizDto>
    fun getAllForLecture(lectureId: Long): List<QuizDto>
    fun add(addRequest: QuizAddRequest): QuizDto
    fun edit(id: Long, editRequest: QuizEditRequest): QuizDto
    fun delete(id: Long): QuizDto
}
