package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionAnswerDto
import sk.streetofcode.webapi.api.request.QuizQuestionAnswerAddRequest
import sk.streetofcode.webapi.api.request.QuizQuestionAnswerEditRequest

interface QuizQuestionAnswerService {
    fun get(id: Long): QuizQuestionAnswerDto
    fun getAll(): List<QuizQuestionAnswerDto>
    fun getAllForQuestion(questionId: Long): List<QuizQuestionAnswerDto>
    fun add(addRequest: QuizQuestionAnswerAddRequest): QuizQuestionAnswerDto
    fun edit(id: Long, editRequest: QuizQuestionAnswerEditRequest): QuizQuestionAnswerDto
    fun delete(id: Long): QuizQuestionAnswerDto
}
