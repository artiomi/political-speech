package com.exercise.political.speech.dispatchers.aggregations

import com.exercise.political.speech.models.AggregationResult
import com.exercise.political.speech.repositories.PoliticalSpeechRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

const val YEAR_COUNT = "YEAR_COUNT"
const val TOPIC_COUNT = "TOPIC_COUNT"
const val WORDS_SUM = "WORDS_SUM"

@Component
class YearCountAggregation(
    private val politicalSpeechRepo: PoliticalSpeechRepo,
    @Value(value = "\${app.speech.aggregation.speech-year}") private val speechYear: Int
) : Aggregation {

    override val componentId = YEAR_COUNT

    override fun execute(): String? {
        val results = politicalSpeechRepo.groupSpeakersPerYearOrderDesc(speechYear)
        return results.getFirsValueIfUniqueOrNull()?.speakerName
    }
}

@Component
class TopicCountAggregation(
    private val politicalSpeechRepo: PoliticalSpeechRepo,
    @Value(value = "\${app.speech.aggregation.speech-topic}") private val speechTopic: String
) : Aggregation {

    override val componentId = TOPIC_COUNT

    override fun execute(): String? {
        val results = politicalSpeechRepo.groupSpeakersPerTopicOrderDesc(speechTopic)
        return results.getFirsValueIfUniqueOrNull()?.speakerName
    }
}

@Component
class WordsSumAggregation(private val politicalSpeechRepo: PoliticalSpeechRepo) : Aggregation {

    override val componentId = WORDS_SUM

    override fun execute(): String? {
        val results = politicalSpeechRepo.groupSpeakersPerWordsSumOrderAsc()
        return results.getFirsValueIfUniqueOrNull()?.speakerName
    }
}

private fun List<AggregationResult>.getFirsValueIfUniqueOrNull(): AggregationResult? {
    return when (size) {
        0 -> null
        1 -> first()
        else -> if (first().aggValue != this[1].aggValue) first() else null
    }
}