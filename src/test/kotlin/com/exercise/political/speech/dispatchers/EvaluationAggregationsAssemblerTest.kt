package com.exercise.political.speech.dispatchers

import com.exercise.political.speech.BATCH_ID
import com.exercise.political.speech.dispatchers.aggregations.*
import com.exercise.political.speech.exceptions.AssembleException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class EvaluationAggregationsAssemblerTest {
    private val mostSpeechesSpeaker = "speaker1"
    private val mostSecuritySpeaker = "speaker2"
    private val leastWordySpeaker = "speaker3"
    private val assembleContext = AssembleContext(BATCH_ID)


    @Test
    fun `throws exception for missing aggregation`() {
        val assembler = EvaluationAggregationsAssembler(emptyList())
        assertThatThrownBy { assembler.assemble(assembleContext) }.isInstanceOf(AssembleException::class.java)
    }

    @Test
    fun `successfully maps all response params`() {
        val assembler = EvaluationAggregationsAssembler(mockAggregations())
        val result = assembler.assemble(assembleContext)
        assertThat(result.size).isEqualTo(3)
        assertThat(result["mostSpeeches"]).isEqualTo(mostSpeechesSpeaker)
        assertThat(result["mostSecurity"]).isEqualTo(mostSecuritySpeaker)
        assertThat(result["leastWordy"]).isEqualTo(leastWordySpeaker)
    }

    private fun mockAggregations(): List<Aggregation> {
        val yearAggregation = mock<Aggregation>()
        whenever(yearAggregation.uniqueId).thenReturn(YEAR_COUNT)
        whenever(yearAggregation.execute(assembleContext)).thenReturn(mostSpeechesSpeaker)

        val topicAggregation = mock<Aggregation>()
        whenever(topicAggregation.uniqueId).thenReturn(TOPIC_COUNT)
        whenever(topicAggregation.execute(assembleContext)).thenReturn(mostSecuritySpeaker)

        val wordsAggregation = mock<Aggregation>()
        whenever(wordsAggregation.uniqueId).thenReturn(WORDS_SUM)
        whenever(wordsAggregation.execute(assembleContext)).thenReturn(leastWordySpeaker)

        return listOf(yearAggregation, topicAggregation, wordsAggregation)
    }
}