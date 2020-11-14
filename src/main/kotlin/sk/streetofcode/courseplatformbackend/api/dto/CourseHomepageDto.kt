package sk.streetofcode.courseplatformbackend.api.dto

import sk.streetofcode.courseplatformbackend.model.Author
import sk.streetofcode.courseplatformbackend.model.Difficulty

data class CourseHomepageDto(
        val id: Long,
        val name: String,
        val shortDescription: String,
        val author: Author? = null,
        val difficulty: Difficulty? = null
)