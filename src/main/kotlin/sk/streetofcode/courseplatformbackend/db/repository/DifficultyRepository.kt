package sk.streetofcode.courseplatformbackend.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.courseplatformbackend.model.Difficulty

@Repository
interface DifficultyRepository : CrudRepository<Difficulty, Long> {
}