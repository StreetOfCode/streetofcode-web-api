package sk.streetofcode.courseplatformbackend.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.courseplatformbackend.api.dto.quiz.QuizQuestionUserAnswerDto
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class QuizQuestionUserAnswer(
    @Id
    @SequenceGenerator(name = "quiz_question_user_answer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_question_user_answer_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    var question: QuizQuestion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    var answer: QuizQuestionAnswer,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false)
    var tryCount: Int
) {
    constructor(question: QuizQuestion, answer: QuizQuestionAnswer, tryCount: Int) :
        this(null, question, answer, OffsetDateTime.now(), tryCount)

    override fun equals(other: Any?) = other is QuizQuestionUserAnswer && QuizQuestionUserAnswerEssential(this) == QuizQuestionUserAnswerEssential(other)
    override fun hashCode() = QuizQuestionUserAnswerEssential(this).hashCode()
    override fun toString() = QuizQuestionUserAnswerEssential(this).toString().replaceFirst("QuizQuestionUserAnswerEssential", "")
}


private data class QuizQuestionUserAnswerEssential(
    val question: QuizQuestion,
    val answer: QuizQuestionAnswer,
    val createdAt: OffsetDateTime,
    val tryCount: Int
) {
    constructor(quizQuestionUserAnswer: QuizQuestionUserAnswer) : this(
        question = quizQuestionUserAnswer.question,
        answer = quizQuestionUserAnswer.answer,
        createdAt = quizQuestionUserAnswer.createdAt,
        tryCount = quizQuestionUserAnswer.tryCount
    )
}

fun QuizQuestionUserAnswer.toQuizQuestionUserAnswerDto(): QuizQuestionUserAnswerDto {
    return QuizQuestionUserAnswerDto(
        id = this.id!!,
        question = this.question.toQuizQuestionDto(),
        answer = this.answer.toQuizQuestionAnswerDto()
    )
}