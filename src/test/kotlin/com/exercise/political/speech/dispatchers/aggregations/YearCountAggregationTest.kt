package com.exercise.political.speech.dispatchers.aggregations

import com.exercise.political.speech.repositories.PoliticalSpeechRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

private const val YEAR_PARAM = 2023

@ExtendWith(MockitoExtension::class)
class YearCountAggregationTest {
    @Mock
    private lateinit var politicalSpeechRepo: PoliticalSpeechRepo
    private lateinit var yearCountAggregation: YearCountAggregation

    @BeforeEach
    fun beforeEach() {
        yearCountAggregation = YearCountAggregation(politicalSpeechRepo, YEAR_PARAM)
    }

    @Test
    fun `returns componentId YEAR_COUNT`() {
        assertThat(yearCountAggregation.componentId).isEqualTo("YEAR_COUNT")
    }

    @Test
    fun `returns null if no speaker found`() {
        whenever(politicalSpeechRepo.countSpeakersSpeechesForYearOrderCountDesc(YEAR_PARAM)).thenReturn(emptyList())
        val speakerName = yearCountAggregation.execute()
        assertThat(speakerName).isNull()
    }

    @Test
    fun `returns first speaker name if only one exist`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 1))
        whenever(politicalSpeechRepo.countSpeakersSpeechesForYearOrderCountDesc(YEAR_PARAM)).thenReturn(aggResult)
        val speakerName = yearCountAggregation.execute()
        assertThat(speakerName).isEqualTo(aggResult.first().speakerName)
    }

    @Test
    fun `returns first speaker name if one with highest count is unique`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 10), AggregationResultImpl("speaker2", 7))
        whenever(politicalSpeechRepo.countSpeakersSpeechesForYearOrderCountDesc(YEAR_PARAM)).thenReturn(aggResult)
        val speakerName = yearCountAggregation.execute()
        assertThat(speakerName).isEqualTo(aggResult.first().speakerName)
    }

    @Test
    fun `returns null if multiple speakers with highest count exist`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 10), AggregationResultImpl("speaker2", 10))
        whenever(politicalSpeechRepo.countSpeakersSpeechesForYearOrderCountDesc(YEAR_PARAM)).thenReturn(aggResult)
        val speakerName = yearCountAggregation.execute()
        assertThat(speakerName).isNull()
    }
}