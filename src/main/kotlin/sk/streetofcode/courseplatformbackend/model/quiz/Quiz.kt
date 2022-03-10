package sk.streetofcode.courseplatformbackend.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizDto
import sk.streetofcode.courseplatformbackend.model.Lecture
import sk.streetofcode.courseplatformbackend.model.toLectureDto
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Quiz(
    @Id
    @SequenceGenerator(name = "quiz_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    val lecture: Lecture,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = true)
    var subtitle: String?,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var createdAt: OffsetDateTime,

    @Column(nullable = true)
    var finishedMessage: String?,

    @OneToMany(
        mappedBy = "quiz",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @OrderBy("question_order")
    var questions: MutableSet<QuizQuestion>
) {
    constructor(lecture: Lecture, title: String, subtitle: String?, createdAt: OffsetDateTime, finishedMessage: String?) :
            this(null, lecture, title, subtitle, createdAt, finishedMessage, mutableSetOf())

    override fun equals(other: Any?) = other is Quiz && QuizEssential(this) == QuizEssential(other)
    override fun hashCode() = QuizEssential(this).hashCode()
    override fun toString() = QuizEssential(this).toString().replaceFirst("QuizEssential", "")
}

private data class QuizEssential(
    val lecture: Lecture,
    val title: String,
    val subtitle: String?,
    val createdAt: OffsetDateTime,
    val finishedMessage: String?
) {
    constructor(quiz: Quiz) : this(
        lecture = quiz.lecture,
        title = quiz.title,
        subtitle = quiz.subtitle,
        createdAt = quiz.createdAt,
        finishedMessage = quiz.finishedMessage
    )
}

fun Quiz.toQuizDto(): QuizDto {
    return QuizDto(
        id = this.id!!,
        lecture = this.lecture.toLectureDto(),
        title = this.title,
        subtitle = this.subtitle,
        createdAt = this.createdAt.truncatedTo(ChronoUnit.SECONDS),
        finishedMessage = this.finishedMessage,
        questions = this.questions.map { it.toQuizQuestionDto() }.toSet()
    )
}

