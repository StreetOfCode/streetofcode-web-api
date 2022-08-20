package sk.streetofcode.webapi.api

import sk.streetofcode.webapi.api.request.DifficultyAddRequest
import sk.streetofcode.webapi.api.request.DifficultyEditRequest
import sk.streetofcode.webapi.model.Difficulty

interface DifficultyService {
    fun get(id: Long): Difficulty
    fun getAll(): List<Difficulty>
    fun add(addRequest: DifficultyAddRequest): Difficulty
    fun edit(id: Long, editRequest: DifficultyEditRequest): Difficulty
    fun delete(id: Long): Difficulty
}
