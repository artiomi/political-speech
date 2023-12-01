package com.exercise.political.speech.dispatchers.readers

import com.exercise.political.speech.exceptions.FileReadException
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import org.springframework.stereotype.Component
import java.io.Reader
import java.time.LocalDate

private const val SEPARATOR = ';'

private const val ROW_SIZE = 4

@Component
class CsvReader : FileReader {
    override fun read(reader: Reader): List<FileRow> {
        val csvParser = CSVParserBuilder().withSeparator(SEPARATOR).build()
        val csvReader = CSVReaderBuilder(reader)
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
        throw FileReadException("Row size should be $ROW_SIZE, but received $size. Row:${this.joinToString()}")
    return FileRow(
        get(0).trim(),
        get(1).trim(),
        get(2).trim().asLocalDate(),
        get(3).trim().toIntOrNull() ?: throw FileReadException("Unable to parse value:[${get(3)}] as Int")
    )
}

private fun String.asLocalDate(): LocalDate {
    try {
        return LocalDate.parse(this)
    } catch (e: Exception) {
        throw FileReadException("Unable to pare value:[$this] as LocalDate. Expected format: yyyy-MM-dd", e)
    }
}
