package com.exercise.political.speech.dispatchers.readers

import java.time.LocalDate

data class FileRow(val speakerName: String, val topic: String, val occurredAt: LocalDate, val wordsCount: Int) {

}
