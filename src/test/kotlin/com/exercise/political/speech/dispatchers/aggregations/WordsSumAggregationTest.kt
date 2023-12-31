package com.exercise.political.speech.dispatchers.aggregations

import com.exercise.political.speech.BATCH_ID
import com.exercise.political.speech.repositories.PoliticalSpeechRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class WordsSumAggregationTest {
    private val assembleContext = AssembleContext(BATCH_ID)

    @Mock
    private lateinit var politicalSpeechRepo: PoliticalSpeechRepo
    private lateinit var wordsSumAggregation: WordsSumAggregation

    @BeforeEach
    fun beforeEach() {
        wordsSumAggregation = WordsSumAggregation(politicalSpeechRepo)
    }

    @Test
    fun `returns componentId WORDS_SUM`() {
        assertThat(wordsSumAggregation.uniqueId).isEqualTo("WORDS_SUM")
    }

    @Test
    fun `returns null if no speaker found`() {
        whenever(politicalSpeechRepo.sumSpeakersWordsOrderSumAsc(BATCH_ID)).thenReturn(emptyList())
        val speakerName = wordsSumAggregation.execute(assembleContext)
        assertThat(speakerName).isNull()
    }

    @Test
    fun `returns first speaker name if only one exist`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 1))
        whenever(politicalSpeechRepo.sumSpeakersWordsOrderSumAsc(BATCH_ID)).thenReturn(aggResult)
        val speakerName = wordsSumAggregation.execute(assembleContext)
        assertThat(speakerName).isEqualTo(aggResult.first().speakerName)
    }

    @Test
    fun `returns first speaker name if one with lowest words sum is unique`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 10), AggregationResultImpl("speaker2", 7))
        whenever(politicalSpeechRepo.sumSpeakersWordsOrderSumAsc(BATCH_ID)).thenReturn(aggResult)
        val speakerName = wordsSumAggregation.execute(assembleContext)
        assertThat(speakerName).isEqualTo(aggResult.first().speakerName)
    }

    @Test
    fun `returns null if multiple speakers with lowest words sum exist`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 10), AggregationResultImpl("speaker2", 10))
        whenever(politicalSpeechRepo.sumSpeakersWordsOrderSumAsc(BATCH_ID)).thenReturn(aggResult)
        val speakerName = wordsSumAggregation.execute(assembleContext)
        assertThat(speakerName).isNull()
    }
}