package com.exercise.political.speech.dispatchers

import com.exercise.political.speech.dispatchers.aggregations.Aggregation
import com.exercise.political.speech.dispatchers.aggregations.TOPIC_COUNT
import com.exercise.political.speech.dispatchers.aggregations.WORDS_SUM
import com.exercise.political.speech.dispatchers.aggregations.YEAR_COUNT
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


    @Test
    fun `throws exception for missing aggregation`() {
        val assembler = EvaluationAggregationsAssembler(emptyList())
        assertThatThrownBy { assembler.assemble() }.isInstanceOf(AssembleException::class.java)
    }

    @Test
    fun `successfully maps all response params`() {
        val assembler = EvaluationAggregationsAssembler(mockAggregations())
        val result = assembler.assemble()
        assertThat(result.size).isEqualTo(3)
        assertThat(result["mostSpeeches"]).isEqualTo(mostSpeechesSpeaker)
        assertThat(result["mostSecurity"]).isEqualTo(mostSecuritySpeaker)
        assertThat(result["leastWordy"]).isEqualTo(leastWordySpeaker)
    }

    private fun mockAggregations(): List<Aggregation> {
        val yearAggregation = mock<Aggregation>()
        whenever(yearAggregation.componentId).thenReturn(YEAR_COUNT)
        whenever(yearAggregation.execute()).thenReturn(mostSpeechesSpeaker)

        val topicAggregation = mock<Aggregation>()
        whenever(topicAggregation.componentId).thenReturn(TOPIC_COUNT)
        whenever(topicAggregation.execute()).thenReturn(mostSecuritySpeaker)

        val wordsAggregation = mock<Aggregation>()
        whenever(wordsAggregation.componentId).thenReturn(WORDS_SUM)
        whenever(wordsAggregation.execute()).thenReturn(leastWordySpeaker)

        return listOf(yearAggregation, topicAggregation, wordsAggregation)
    }
}