package com.exercise.political.speech.services

import com.exercise.political.speech.dispatchers.readers.FileRow
import com.exercise.political.speech.models.PoliticalSpeech
import com.exercise.political.speech.repositories.PoliticalSpeechRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PoliticalSpeechSvc(private val politicalSpeechRepo: PoliticalSpeechRepo) {

    @Transactional
    fun saveAllInBatch(rows: List<FileRow>, batchId: String) {
        val speeches = rows.map { it.toPoliticalSpeech(batchId) }
        politicalSpeechRepo.saveAll(speeches)
    }
}

private fun FileRow.toPoliticalSpeech(batchId: String) =
    PoliticalSpeech(
        speakerName = speakerName,
        topic = topic,
        occurredAt = occurredAt,
        wordsCount = wordsCount,
        batchId = batchId
    )

