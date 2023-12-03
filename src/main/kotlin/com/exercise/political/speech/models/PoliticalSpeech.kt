package com.exercise.political.speech.models

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "political_speeches")
class PoliticalSpeech(

    @Column(name = "speaker_name")
    val speakerName: String,
    val topic: String,
    @Column(name = "occurred_at")
    val occurredAt: LocalDate,
    @Column(name = "words_count")
    val wordsCount: Int,
    @Column(name = "batch_id")
    val batchId: String,
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
)