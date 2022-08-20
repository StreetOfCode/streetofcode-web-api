package sk.streetofcode.webapi.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.webapi.model.quiz.Quiz

interface QuizRepository : CrudRepository<Quiz, Long> {
    fun findByLectureId(lectureId: Long): Set<Quiz>
}
