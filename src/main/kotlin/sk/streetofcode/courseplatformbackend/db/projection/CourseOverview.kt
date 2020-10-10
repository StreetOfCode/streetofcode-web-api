package sk.streetofcode.courseplatformbackend.db.projection

import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Difficulty
import java.time.OffsetDateTime

interface CourseOverview {
    fun getId(): Long
    fun getName(): String
    fun getShortDescription(): String
    fun getLongDescription(): String
    fun getAuthor(): Author
    fun getDifficulty(): Difficulty
    fun getCreatedAt(): OffsetDateTime
    fun getUpdatedAt(): OffsetDateTime
    fun getChapters(): List<ChapterOverview>
}

