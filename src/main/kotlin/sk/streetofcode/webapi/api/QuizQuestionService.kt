package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionDto
import sk.streetofcode.webapi.api.request.QuizQuestionAddRequest
import sk.streetofcode.webapi.api.request.QuizQuestionEditRequest

interface QuizQuestionService {
    fun get(id: Long): QuizQuestionDto
    fun getAll(): List<QuizQuestionDto>
    fun getAllForQuiz(quizId: Long): List<QuizQuestionDto>
    fun add(addRequest: QuizQuestionAddRequest): QuizQuestionDto
    fun edit(id: Long, editRequest: QuizQuestionEditRequest): QuizQuestionDto
    fun delete(id: Long): QuizQuestionDto
}
