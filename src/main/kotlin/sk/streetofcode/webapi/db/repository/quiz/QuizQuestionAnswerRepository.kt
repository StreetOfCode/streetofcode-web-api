package sk.streetofcode.webapi.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.webapi.model.quiz.QuizQuestionAnswer

interface QuizQuestionAnswerRepository : CrudRepository<QuizQuestionAnswer, Long> {
    fun findByQuestionId(questionId: Long): List<QuizQuestionAnswer>
}
