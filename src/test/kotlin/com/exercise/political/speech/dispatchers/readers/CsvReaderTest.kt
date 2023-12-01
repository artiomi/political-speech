package com.exercise.political.speech.dispatchers.readers

import com.exercise.political.speech.exceptions.FileReadException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CsvReaderTest {
    private val standardInputCsv = """
    Speaker;Topic;Date;Words
    Alexander Abel; education ; 2012-10-30; 5310
    """.trimIndent()

    private val lessColumnsInputCsv = """
    Speaker;Topic;Date;Words
    Alexander Abel; education ; 2012-10-30
    """.trimIndent()

    private val invalidDateInputCsv = """
    Speaker;Topic;Date;Words
    Alexander Abel; education ; 20121030; 5310
    """.trimIndent()

    private val invalidWordsCountInputCsv = """
    Speaker;Topic;Date;Words
    Alexander Abel; education ; 2012-10-30; abcd
    """.trimIndent()

    private val invalidSeparatorInputCsv = """
    Speaker,Topic,Date,Words
    Alexander Abel, education , 2012-10-30, 5310
    """.trimIndent()

    private val csvReader = CsvReader()

    @Test
    fun `successfully read row from standard input`() {
        val expected = FileRow("Alexander Abel", "education", LocalDate.parse("2012-10-30"), 5310)
        val testReader = standardInputCsv.byteInputStream().reader()
        val fileRows = csvReader.read(testReader)

        assertThat(fileRows).hasSize(1)
        assertThat(fileRows).anyMatch { it == expected }
    }

    @Test
    fun `return empty result for for empty input`() {
        val testReader = "".byteInputStream().reader()
        assertThat(csvReader.read(testReader))
            .isEmpty()
    }

    @Test
    fun `throws exception for input with less values`() {
        val testReader = lessColumnsInputCsv.byteInputStream().reader()
        assertThatThrownBy { csvReader.read(testReader) }
            .isInstanceOf(FileReadException::class.java)
            .hasMessageStartingWith("Row size should be 4")
    }

    @Test
    fun `throws exception for invalid date`() {
        val testReader = invalidDateInputCsv.byteInputStream().reader()
        assertThatThrownBy { csvReader.read(testReader) }
            .isInstanceOf(FileReadException::class.java)
            .hasMessage("Unable to pare value:[20121030] as LocalDate. Expected format: yyyy-MM-dd")
    }

    @Test
    fun `throws exception for invalid words count`() {
        val testReader = invalidWordsCountInputCsv.byteInputStream().reader()
        assertThatThrownBy { csvReader.read(testReader) }
            .isInstanceOf(FileReadException::class.java)
            .hasMessage("Unable to parse value:[ abcd] as Int")
    }

    @Test
    fun `throws exception for invalid separator`() {
        val testReader = invalidSeparatorInputCsv.byteInputStream().reader()
        assertThatThrownBy { csvReader.read(testReader) }
            .isInstanceOf(FileReadException::class.java)
            .hasMessageStartingWith("Row size should be 4")
    }
}