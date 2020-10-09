package sk.streetofcode.courseplatformbackend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Lecture(
        @Id
        @SequenceGenerator(name = "lecture_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_id_seq")
        val id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "chapter_id", nullable = false)
        val chapter: Chapter,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val lectureOrder: Int,

        @Column(nullable = false)
        val content: String,

        @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val createdAt: OffsetDateTime,

        @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val updatedAt: OffsetDateTime
) {
    constructor(chapter: Chapter, name: String, lectureOrder: Int, content: String)
            : this(null, chapter, name, lectureOrder, content, OffsetDateTime.now(), OffsetDateTime.now())
}