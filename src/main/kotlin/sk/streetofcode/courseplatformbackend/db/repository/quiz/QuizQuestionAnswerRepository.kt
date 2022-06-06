package sk.streetofcode.courseplatformbackend.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestionAnswer

interface QuizQuestionAnswerRepository : CrudRepository<QuizQuestionAnswer, Long> {
    fun findByQuestionId(questionId: Long): List<QuizQuestionAnswer>
}
