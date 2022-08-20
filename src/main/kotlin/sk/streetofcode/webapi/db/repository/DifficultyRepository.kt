package sk.streetofcode.webapi.db.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sk.streetofcode.webapi.model.Difficulty

@Repository
interface DifficultyRepository : CrudRepository<Difficulty, Long>
