package sk.streetofcode.webapi.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.webapi.model.quiz.QuizQuestion

interface QuizQuestionRepository : CrudRepository<QuizQuestion, Long> {
    fun findByQuizId(quizId: Long): List<QuizQuestion>
}
