package sk.streetofcode.courseplatformbackend.api

import sk.streetofcode.courseplatformbackend.api.request.DifficultyAddRequest
import sk.streetofcode.courseplatformbackend.api.request.DifficultyEditRequest
import sk.streetofcode.courseplatformbackend.model.Difficulty

interface DifficultyService {
    fun get(id: Long): Difficulty
    fun getAll(): List<Difficulty>
    fun add(addRequest: DifficultyAddRequest): Difficulty
    fun edit(id: Long, editRequest: DifficultyEditRequest): Difficulty
    fun delete(id: Long)
}