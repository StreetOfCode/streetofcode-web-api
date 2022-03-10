package sk.streetofcode.courseplatformbackend.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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
    var isMultipleChoice: Boolean
) {
    constructor(quiz: Quiz, questionOrder: Int, text: String, isMultipleChoice: Boolean) :
            this(null, quiz, questionOrder, text, isMultipleChoice)

    override fun equals(other: Any?) = other is QuizQuestion && QuizQuestionEssential(this) == QuizQuestionEssential(other)
    override fun hashCode() = QuizQuestionEssential(this).hashCode()
    override fun toString() = QuizQuestionEssential(this).toString().replaceFirst("QuizQuestionEssential", "")
}

private data class QuizQuestionEssential(
    val quiz: Quiz,
    val questionOrder: Int,
    val text: String,
    val isMultipleChoice: Boolean
) {
    constructor(question: QuizQuestion) : this(
        quiz = question.quiz,
        questionOrder = question.questionOrder,
        text = question.text,
        isMultipleChoice = question.isMultipleChoice
    )
}

fun QuizQuestion.toQuizQuestionDto(): QuizQuestionDto {
    return QuizQuestionDto(
        id = this.id!!,
        quiz = this.quiz.toQuizDto(),
        questionOrder = this.questionOrder,
        text = this.text,
        isMultipleChoice = this.isMultipleChoice
    )
}