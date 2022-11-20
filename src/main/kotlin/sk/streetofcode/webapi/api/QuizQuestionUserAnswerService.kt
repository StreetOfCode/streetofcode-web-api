package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionAnswerCorrectnessDto
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionUserAnswerDto
import sk.streetofcode.webapi.api.request.QuizQuestionUserAnswerRequest

interface QuizQuestionUserAnswerService {
    fun get(id: Long): QuizQuestionUserAnswerDto
    fun getAll(): List<QuizQuestionUserAnswerDto>
    fun getAllAnswersForQuestion(questionId: Long): List<QuizQuestionUserAnswerDto>
    fun getAllUserAnswersForQuiz(quizId: Long): List<QuizQuestionUserAnswerDto>
    fun answer(answerRequest: QuizQuestionUserAnswerRequest): QuizQuestionAnswerCorrectnessDto
    fun removeAllUserAnswersByLectureId(lectureId: Long)
}
