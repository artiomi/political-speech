package com.exercise.political.speech.services

import com.exercise.political.speech.dispatchers.readers.FileRow
import com.exercise.political.speech.models.PoliticalSpeech
import com.exercise.political.speech.repositories.PoliticalSpeechRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PoliticalSpeechSvc(val politicalSpeechRepo: PoliticalSpeechRepo) {

    @Transactional
    fun cleanAndSave(rows: List<FileRow>) {
        politicalSpeechRepo.deleteAll()
        val speeches = rows.map { it.toDomain() }
        politicalSpeechRepo.saveAll(speeches)
    }
}

private fun FileRow.toDomain() =
    PoliticalSpeech(speakerName = speakerName, topic = topic, occurredAt = occurredAt, wordsCount = wordsCount)

