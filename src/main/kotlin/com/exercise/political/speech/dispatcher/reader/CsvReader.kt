package com.exercise.political.speech.dispatcher.reader

import com.exercise.political.speech.exception.FileReadException
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.time.LocalDate

private const val SEPARATOR = ';'

private const val ROW_SIZE = 4

@Component
class CsvReader : FileReader {
    override fun read(input: BufferedReader): List<FileRow> {
        val csvParser = CSVParserBuilder().withSeparator(SEPARATOR).build()
        val csvReader = CSVReaderBuilder(input)
            .withCSVParser(csvParser)
            .withSkipLines(1)
            .build()

        return csvReader.asSequence()
            .filterNotNull()
            .map { it.toFileRow() }
            .toList()

    }
}

private fun Array<String>.toFileRow(): FileRow {
    if (size != ROW_SIZE)
        throw IllegalArgumentException("Row size should be $ROW_SIZE, but received $size. Row:${this.joinToString()}")
    return FileRow(
        get(0).trim(),
        get(1).trim(),
        get(2).trim().asLocalDate(),
        get(3).trim().toIntOrNull() ?: throw FileReadException("Unable to parse value:[${get(3)}] as Int.")
    )
}

private fun String.asLocalDate(): LocalDate {
    val date: LocalDate?
    try {
        date = LocalDate.parse(this)
    } catch (e: Exception) {
        throw FileReadException("Unable to pare value:[$this] as LocalDate.", e)
    }
    return date ?: throw FileReadException("LocalDate can't be null.")
}
