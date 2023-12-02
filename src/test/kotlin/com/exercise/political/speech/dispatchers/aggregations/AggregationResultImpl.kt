package com.exercise.political.speech.dispatchers.aggregations

import com.exercise.political.speech.models.AggregationResult

class AggregationResultImpl(override val speakerName: String, override val aggValue: Int) :AggregationResult {
}