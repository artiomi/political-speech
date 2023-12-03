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

private const val TOPIC_VALUE = "test-topic"

@ExtendWith(MockitoExtension::class)
class TopicCountAggregationTest {
    private val assembleContext = AssembleContext(BATCH_ID)

    @Mock
    private lateinit var politicalSpeechRepo: PoliticalSpeechRepo
    private lateinit var topicCountAggregation: TopicCountAggregation

    @BeforeEach
    fun beforeEach() {
        topicCountAggregation = TopicCountAggregation(politicalSpeechRepo, TOPIC_VALUE)
    }

    @Test
    fun `returns componentId TOPIC_COUNT`() {
        assertThat(topicCountAggregation.uniqueId).isEqualTo("TOPIC_COUNT")
    }

    @Test
    fun `returns null if no speaker found`() {
        whenever(politicalSpeechRepo.countSpeakerSpeechesForTopicOrderCountDesc(TOPIC_VALUE, BATCH_ID))
            .thenReturn(emptyList())
        val speakerName = topicCountAggregation.execute(assembleContext)
        assertThat(speakerName).isNull()
    }

    @Test
    fun `returns first speaker name if only one exist`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 1))
        whenever(politicalSpeechRepo.countSpeakerSpeechesForTopicOrderCountDesc(TOPIC_VALUE, BATCH_ID))
            .thenReturn(aggResult)
        val speakerName = topicCountAggregation.execute(assembleContext)
        assertThat(speakerName).isEqualTo(aggResult.first().speakerName)
    }

    @Test
    fun `returns first speaker name if one with highest count is unique`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 10), AggregationResultImpl("speaker2", 7))
        whenever(politicalSpeechRepo.countSpeakerSpeechesForTopicOrderCountDesc(TOPIC_VALUE, BATCH_ID))
            .thenReturn(aggResult)
        val speakerName = topicCountAggregation.execute(assembleContext)
        assertThat(speakerName).isEqualTo(aggResult.first().speakerName)
    }

    @Test
    fun `returns null if multiple speakers with highest count exist`() {
        val aggResult = listOf(AggregationResultImpl("speaker1", 10), AggregationResultImpl("speaker2", 10))
        whenever(politicalSpeechRepo.countSpeakerSpeechesForTopicOrderCountDesc(TOPIC_VALUE, BATCH_ID))
            .thenReturn(aggResult)
        val speakerName = topicCountAggregation.execute(assembleContext)
        assertThat(speakerName).isNull()
    }
}

