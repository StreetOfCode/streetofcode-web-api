package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionAnswerCorrectnessDto
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionUserAnswerDto
import sk.streetofcode.courseplatformbackend.api.request.QuizQuestionUserAnswerRequest

interface QuizQuestionUserAnswerService {
    fun get(id: Long): QuizQuestionUserAnswerDto
    fun getAll(): List<QuizQuestionUserAnswerDto>
    fun getAllAnswersForQuestion(questionId: Long): List<QuizQuestionUserAnswerDto>
    fun getAllUserAnswersForQuiz(quizId: Long): List<QuizQuestionUserAnswerDto>
    fun answer(answerRequest: QuizQuestionUserAnswerRequest): QuizQuestionAnswerCorrectnessDto
}
