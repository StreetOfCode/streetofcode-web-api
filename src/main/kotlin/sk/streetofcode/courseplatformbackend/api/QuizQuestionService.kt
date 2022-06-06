package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionDto
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionAddRequest
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionEditRequest

interface QuizQuestionService {
    fun get(id: Long): QuizQuestionDto
    fun getAll(): List<QuizQuestionDto>
    fun getAllForQuiz(quizId: Long): List<QuizQuestionDto>
    fun add(addRequest: QuizQuestionAddRequest): QuizQuestionDto
    fun edit(id: Long, editRequest: QuizQuestionEditRequest): QuizQuestionDto
    fun delete(id: Long): QuizQuestionDto
}
