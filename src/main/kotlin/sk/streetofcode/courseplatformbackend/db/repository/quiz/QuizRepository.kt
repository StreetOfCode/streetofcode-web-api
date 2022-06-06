package sk.streetofcode.courseplatformbackend.db.repository.quiz

import org.springframework.data.repository.CrudRepository
import sk.streetofcode.courseplatformbackend.model.quiz.Quiz

interface QuizRepository : CrudRepository<Quiz, Long> {
    fun findByLectureId(lectureId: Long): Set<Quiz>
}
