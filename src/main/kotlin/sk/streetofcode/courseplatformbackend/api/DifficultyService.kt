package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.model.Difficulty

interface DifficultyService {
    fun get(id: Long): Difficulty
    fun getAll(): List<Difficulty>
    fun add(addRequest: DifficultyAddRequest): Long
    fun delete(id: Long)
}