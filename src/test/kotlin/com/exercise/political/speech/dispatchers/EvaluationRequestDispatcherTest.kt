package com.exercise.political.speech.dispatchers

import com.exercise.political.speech.BATCH_ID
import com.exercise.political.speech.controllers.EvaluationRequest
import com.exercise.political.speech.controllers.EvaluationRequests
import com.exercise.political.speech.controllers.UriSchema
import com.exercise.political.speech.dispatchers.aggregations.AssembleContext
import com.exercise.political.speech.dispatchers.parsers.FileParser
import com.exercise.political.speech.dispatchers.readers.FileRow
import com.exercise.political.speech.exceptions.AssembleException
import com.exercise.political.speech.exceptions.FileReadException
import com.exercise.political.speech.services.PoliticalSpeechSvc
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.net.URI
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class EvaluationRequestDispatcherTest {
    @Mock
    private lateinit var fileParser: FileParser

    @Mock
    private lateinit var politicalSpeechSvc: PoliticalSpeechSvc

    @Mock
    private lateinit var aggregationsAssembler: EvaluationAggregationsAssembler
    private lateinit var requestDispatcher: EvaluationRequestDispatcher

    private val fileRow = FileRow("speaker", "topic", LocalDate.parse("2023-12-01"), 1)
    private val evaluationRequest = EvaluationRequest(UriSchema.FILE, URI(""))
    private val assembleResponse = mapOf("mostSpeeches" to "speaker1")
    private val assembleContext = AssembleContext(BATCH_ID)
    private val evaluationRequests = EvaluationRequests(listOf(evaluationRequest), BATCH_ID)

    @BeforeEach
    fun beforeEach() {
        requestDispatcher =
            EvaluationRequestDispatcher(listOf(fileParser), politicalSpeechSvc, aggregationsAssembler)
    }

    @Test
    fun `successfully process requests`() {
        whenever(fileParser.supports(evaluationRequest)).thenReturn(true)
        whenever(fileParser.parse(evaluationRequest)).thenReturn(listOf(fileRow))
        whenever(aggregationsAssembler.assemble(assembleContext)).thenReturn(assembleResponse)

        val result = requestDispatcher.dispatch(evaluationRequests)
        assertThat(result).isEqualTo(assembleResponse)
        verify(politicalSpeechSvc).saveAllInBatch(eq(listOf(fileRow)), eq(BATCH_ID))
    }

    @Test
    fun `throws exception when matching file parser is missing`() {
        assertThatThrownBy { requestDispatcher.dispatch(evaluationRequests) }
            .isInstanceOf(FileReadException::class.java)
            .hasMessageStartingWith("Unable to find parser for request")

        verify(fileParser).supports(evaluationRequest)
        verify(fileParser, never()).parse(any())
        verify(politicalSpeechSvc, never()).saveAllInBatch(any(), eq(BATCH_ID))
        verify(aggregationsAssembler, never()).assemble(any<AssembleContext>())
    }

    @Test
    fun `throws exception if file parsing fails`() {
        whenever(fileParser.supports(evaluationRequest)).thenReturn(true)
        whenever(fileParser.parse(evaluationRequest)).thenThrow(FileReadException::class.java)

        assertThatThrownBy { requestDispatcher.dispatch(evaluationRequests) }
            .isInstanceOf(FileReadException::class.java)

        verify(politicalSpeechSvc, never()).saveAllInBatch(any(), eq(BATCH_ID))
        verify(aggregationsAssembler, never()).assemble(any<AssembleContext>())
    }

    @Test
    fun `throws exception if saveAllInBatch fails`() {
        whenever(fileParser.supports(evaluationRequest)).thenReturn(true)
        whenever(fileParser.parse(evaluationRequest)).thenReturn(listOf(fileRow))
        whenever(politicalSpeechSvc.saveAllInBatch(eq(listOf(fileRow)), eq(BATCH_ID)))
            .thenThrow(RuntimeException::class.java)

        assertThatThrownBy { requestDispatcher.dispatch(evaluationRequests) }
            .isInstanceOf(RuntimeException::class.java)

        verify(aggregationsAssembler, never()).assemble(any<AssembleContext>())
    }

    @Test
    fun `throws exception if response assemble fails`() {
        whenever(fileParser.supports(evaluationRequest)).thenReturn(true)
        whenever(fileParser.parse(evaluationRequest)).thenReturn(listOf(fileRow))
        whenever(aggregationsAssembler.assemble(assembleContext)).thenThrow(AssembleException::class.java)

        assertThatThrownBy { requestDispatcher.dispatch(evaluationRequests) }
            .isInstanceOf(AssembleException::class.java)
        verify(politicalSpeechSvc).saveAllInBatch(eq(listOf(fileRow)), eq(BATCH_ID))
    }
}