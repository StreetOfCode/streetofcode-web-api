package sk.streetofcode.courseplatformbackend.db.projection.homepage

import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Difficulty

interface CoursesHomepage {
    fun getId(): Long
    fun getName(): String
    fun getShortDescription(): String
    fun getAuthor(): Author
    fun getDifficulty(): Difficulty
}