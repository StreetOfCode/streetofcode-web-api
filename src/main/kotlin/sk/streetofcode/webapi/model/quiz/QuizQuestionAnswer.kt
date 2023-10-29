package sk.streetofcode.webapi.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionAnswerDto
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class QuizQuestionAnswer(
    @Id
    @SequenceGenerator(name = "quiz_question_answer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_question_answer_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_question_id", nullable = false)
    var question: QuizQuestion,

    @Column(nullable = false, columnDefinition = "TEXT")
    var text: String,

    @Column(nullable = false)
    var isCorrect: Boolean,

    @OneToMany(
        mappedBy = "answer",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val userAnswers: MutableSet<QuizQuestionUserAnswer>,
) {
    constructor(question: QuizQuestion, text: String, isCorrect: Boolean) :
        this(null, question, text, isCorrect, mutableSetOf())

    override fun equals(other: Any?) =
        other is QuizQuestionAnswer && QuizQuestionAnswerEssential(this) == QuizQuestionAnswerEssential(other)

    override fun hashCode() = QuizQuestionAnswerEssential(this).hashCode()
    override fun toString() =
        QuizQuestionAnswerEssential(this).toString().replaceFirst("QuizQuestionAnswerEssential", "QuizQuestionAnswer")
}

private data class QuizQuestionAnswerEssential(
    val questionId: Long,
    val text: String,
    val isCorrect: Boolean
) {
    constructor(quizQuestionAnswer: QuizQuestionAnswer) : this(
        questionId = quizQuestionAnswer.question.id!!,
        text = quizQuestionAnswer.text,
        isCorrect = quizQuestionAnswer.isCorrect
    )
}

fun QuizQuestionAnswer.toQuizQuestionAnswerDto(isAdmin: Boolean? = false): QuizQuestionAnswerDto {
    return QuizQuestionAnswerDto(
        id = this.id!!,
        questionId = this.question.id!!,
        text = this.text,
        isCorrect = if (isAdmin == true) {
            this.isCorrect
        } else {
            null
        }
    )
}
