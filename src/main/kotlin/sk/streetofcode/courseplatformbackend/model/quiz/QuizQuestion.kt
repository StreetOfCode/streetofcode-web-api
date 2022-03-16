package sk.streetofcode.courseplatformbackend.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Where
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionDto
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class QuizQuestion(
    @Id
    @SequenceGenerator(name = "quiz_question_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_question_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    var quiz: Quiz,

    @Column(nullable = false)
    var questionOrder: Int,

    @Column(nullable = false)
    var text: String,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var type: QuizQuestionType,

    @OneToMany(
        mappedBy = "question",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val answers: MutableSet<QuizQuestionAnswer>,

    @OneToMany(
        mappedBy = "question",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @Where(clause = "is_correct = true")
    val correctAnswers: MutableSet<QuizQuestionAnswer>
) {
    constructor(quiz: Quiz, questionOrder: Int, text: String, type: QuizQuestionType) :
        this(null, quiz, questionOrder, text, type, mutableSetOf(), mutableSetOf())

    override fun equals(other: Any?) = other is QuizQuestion && QuizQuestionEssential(this) == QuizQuestionEssential(other)
    override fun hashCode() = QuizQuestionEssential(this).hashCode()
    override fun toString() = QuizQuestionEssential(this).toString().replaceFirst("QuizQuestionEssential", "QuizQuestion")
}

private data class QuizQuestionEssential(
    val quizId: Long,
    val questionOrder: Int,
    val text: String,
    val type: QuizQuestionType
) {
    constructor(question: QuizQuestion) : this(
        quizId = question.quiz.id!!,
        questionOrder = question.questionOrder,
        text = question.text,
        type = question.type
    )
}

fun QuizQuestion.toQuizQuestionDto(): QuizQuestionDto {
    return QuizQuestionDto(
        id = this.id!!,
        quiz = this.quiz.toQuizDto(),
        questionOrder = this.questionOrder,
        text = this.text,
        type = this.type,
        answers = this.answers.map { it.toQuizQuestionAnswerDto() }.toSet()
    )
}

enum class QuizQuestionType {
    SINGLE_CHOICE, MULTIPLE_CHOICE
}
