package sk.streetofcode.courseplatformbackend.api.mapper

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import sk.streetofcode.courseplatformbackend.api.dto.ChapterDto
import sk.streetofcode.courseplatformbackend.model.Chapter

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
interface ChapterMapper {
    fun toChapterDto(chapter: Chapter): ChapterDto
}