package sk.streetofcode.courseplatformbackend.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.courseplatformbackend.model.quiz.QuizQuestionUserAnswer
import java.util.*
import javax.transaction.Transactional

interface QuizQuestionUserAnswerRepository : CrudRepository<QuizQuestionUserAnswer, Long> {
    fun findByQuestionIdAndUserId(questionId: Long, userId: UUID): List<QuizQuestionUserAnswer>
    fun findByQuestionId(questionId: Long): List<QuizQuestionUserAnswer>
    fun findByQuestionQuizIdAndUserId(quizId: Long, userUUID: UUID): List<QuizQuestionUserAnswer>

    @Transactional
    fun deleteByQuestionIdAndUserId(answerId: Long, userId: UUID): Int
}
