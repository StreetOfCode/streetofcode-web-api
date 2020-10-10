package sk.streetofcode.courseplatformbackend.db.projection

interface ChapterOverview {
    fun getId(): Long
    fun getName(): String
    fun getLectures(): List<LectureOverview>
}