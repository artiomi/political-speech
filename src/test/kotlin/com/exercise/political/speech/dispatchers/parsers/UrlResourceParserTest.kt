package com.exercise.political.speech.dispatchers.parsers

import com.exercise.political.speech.controllers.EvaluationRequest
import com.exercise.political.speech.controllers.UriSchema
import com.exercise.political.speech.dispatchers.readers.FileReader
import com.exercise.political.speech.dispatchers.readers.FileRow
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.isA
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.FileNotFoundException
import java.io.Reader
import java.net.URI
import java.nio.file.Path
import java.time.LocalDate

@ExtendWith(value = [MockitoExtension::class])
class UrlResourceParserTest {
    @Mock
    private lateinit var fileReader: FileReader

    @TempDir
    private lateinit var tempPath: Path
    private lateinit var urlResourceParser: UrlResourceParser

    @BeforeEach
    fun beforeEach() {
        urlResourceParser = UrlResourceParser(fileReader)
    }

    @Nested
    inner class ParseTest {
        @Test
        fun `successfully parse rows`() {
            val request = EvaluationRequest(UriSchema.FILE, tempPath.toUri())
            val fileRow = FileRow("speaker", "topic", LocalDate.parse("2023-12-01"), 1)
            whenever(fileReader.read(isA<Reader>())).thenReturn(listOf(fileRow))

            val result = urlResourceParser.parse(request)
            assertThat(result).hasSize(1)
            assertThat(result).first().matches { it == fileRow }
        }

        @Test
        fun `throws exception during accessing missing url`() {
            val request = EvaluationRequest(UriSchema.FILE, URI("file:///input"))

            assertThatThrownBy { urlResourceParser.parse(request) }
                .isInstanceOf(FileNotFoundException::class.java)
            verify(fileReader, never()).read(isA<Reader>())
        }
    }

    @Nested
    inner class SupportsTest {

        @ParameterizedTest
        @EnumSource(value = UriSchema::class)
        fun `returns true for acceptable requests`(schema: UriSchema) {
            val request = EvaluationRequest(schema, tempPath.toUri())
            val supports = urlResourceParser.supports(request)
            assertThat(supports).isTrue()
        }
    }
}