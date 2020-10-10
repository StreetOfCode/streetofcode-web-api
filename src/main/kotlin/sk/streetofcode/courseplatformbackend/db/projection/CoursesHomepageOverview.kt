package sk.streetofcode.courseplatformbackend.db.projection

import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Difficulty

interface CoursesHomepageOverview {
    fun getId(): Long
    fun getName(): String
    fun getShortDescription(): String
    fun getAuthor(): Author
    fun getDifficulty(): Difficulty
}