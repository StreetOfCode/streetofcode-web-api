package sk.streetofcode.webapi.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.webapi.model.quiz.QuizQuestionUserAnswer
import javax.transaction.Transactional

interface QuizQuestionUserAnswerRepository : CrudRepository<QuizQuestionUserAnswer, Long> {
    fun findByQuestionIdAndUserId(questionId: Long, userId: String): List<QuizQuestionUserAnswer>
    fun findByQuestionId(questionId: Long): List<QuizQuestionUserAnswer>
    fun findByQuestionQuizIdAndUserId(quizId: Long, userId: String): List<QuizQuestionUserAnswer>

    @Transactional
    fun deleteByQuestionIdAndUserId(answerId: Long, userId: String): Int

    @Transactional
    fun deleteByQuestionQuizLectureIdAndUserId(lectureId: Long, userId: String): Int
}
