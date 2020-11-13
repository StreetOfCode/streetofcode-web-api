package sk.streetofcode.courseplatformbackend.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import sk.streetofcode.courseplatformbackend.api.dto.CourseDto
import sk.streetofcode.courseplatformbackend.model.Course

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
interface CourseMapper {
    fun toCourseDto(course: Course): CourseDto
}