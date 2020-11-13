package sk.streetofcode.courseplatformbackend.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import sk.streetofcode.courseplatformbackend.api.dto.LectureDto
import sk.streetofcode.courseplatformbackend.model.Lecture

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
interface LectureMapper {
    fun toLectureDto(lecture: Lecture): LectureDto
}