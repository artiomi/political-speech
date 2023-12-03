package com.exercise.political.speech.dispatchers

import com.exercise.political.speech.dispatchers.aggregations.*
import com.exercise.political.speech.exceptions.AssembleException
import org.springframework.stereotype.Component

@Component
class EvaluationAggregationsAssembler(private val aggregations: List<Aggregation>) {
    val responseFieldsRegistry = mapOf(
        "mostSpeeches" to YEAR_COUNT,
        "mostSecurity" to TOPIC_COUNT,
        "leastWordy" to WORDS_SUM
    )

    fun assemble(assembleContext: AssembleContext): Map<String, String?> {
        val result = mutableMapOf<String, String?>()
        for ((field, componentId) in responseFieldsRegistry) {
            val aggregation = getAggregationComponent(field, componentId)
            val value = aggregation.execute(assembleContext)
            result[field] = value
        }
        return result
    }

    fun getAggregationComponent(attribute: String, id: String): Aggregation {
        return aggregations
            .firstOrNull { it.uniqueId == id }
            ?: throw AssembleException("Unable to find handler for attribute:$attribute")
    }
}