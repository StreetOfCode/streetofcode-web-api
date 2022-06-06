package sk.streetofcode.courseplatformbackend.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestion

interface QuizQuestionRepository : CrudRepository<QuizQuestion, Long> {
    fun findByQuizId(quizId: Long): List<QuizQuestion>
}
