package sk.streetofcode.courseplatformbackend.service

import org.springframework.stereotype.Service
import sk.streetofcode.courseplatformbackend.api.LectureService
import sk.streetofcode.courseplatformbackend.api.exception.InternalErrorException
import sk.streetofcode.courseplatformbackend.api.exception.ResourceNotFoundException
import sk.streetofcode.courseplatformbackend.api.request.LectureAddRequest
import sk.streetofcode.courseplatformbackend.db.repository.ChapterRepository
import sk.streetofcode.courseplatformbackend.db.repository.LectureRepository
import sk.streetofcode.courseplatformbackend.model.Lecture

@Service
class LectureServiceImpl(val lectureRepository: LectureRepository, val chapterRepository: ChapterRepository) : LectureService {
    override fun get(id: Long): Lecture {
        return lectureRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("Lecture with id $id was not found") }
    }

    override fun getAll(): List<Lecture> {
        return lectureRepository.findAll().toList()
    }

    override fun add(addRequest: LectureAddRequest): Long {
        val chapter = chapterRepository.findById(addRequest.chapterId)
        if (chapter.isEmpty) {
            throw ResourceNotFoundException("Chapter with id ${addRequest.chapterId} was not found")
        }

        val lecture = Lecture(chapter.get(), addRequest.name, addRequest.lectureOrder, addRequest.content)
        return lectureRepository.save(lecture).id ?: throw InternalErrorException("Could not save lecture")
    }

    override fun delete(id: Long) {
        if (lectureRepository.existsById(id)) {
            lectureRepository.deleteById(id)
        } else {
            throw ResourceNotFoundException("Lecture with id $id was not found")
        }
    }

}