package sk.streetofcode.courseplatformbackend.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionAnswerCorrectnessDto
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionAnswerDto
import javax.persistence.*

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

    @Column(nullable = false)
    var text: String,

    @Column(nullable = false)
    var isCorrect: Boolean
) {
    constructor(question: QuizQuestion, text: String, isCorrect: Boolean) :
        this(null, question, text, isCorrect)

    override fun equals(other: Any?) = other is QuizQuestionAnswer && QuizQuestionAnswerEssential(this) == QuizQuestionAnswerEssential(other)
    override fun hashCode() = QuizQuestionAnswerEssential(this).hashCode()
    override fun toString() = QuizQuestionAnswerEssential(this).toString().replaceFirst("QuizQuestionAnswerEssential", "QuizQuestionAnswer")
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

fun QuizQuestionAnswer.toQuizQuestionAnswerDto(): QuizQuestionAnswerDto {
    return QuizQuestionAnswerDto(
        id = this.id!!,
        questionId = this.question.id!!,
        text = this.text
    )
}

fun QuizQuestionAnswer.toCorrectnessDto(): QuizQuestionAnswerCorrectnessDto {
    return QuizQuestionAnswerCorrectnessDto(
        isCorrect = this.isCorrect
    )
}
