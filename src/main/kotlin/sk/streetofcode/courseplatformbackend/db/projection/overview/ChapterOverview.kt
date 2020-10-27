package sk.streetofcode.courseplatformbackend.db.projection.overview

interface ChapterOverview {
    fun getId(): Long
    fun getName(): String
    fun getLectures(): List<LectureOverview>
}