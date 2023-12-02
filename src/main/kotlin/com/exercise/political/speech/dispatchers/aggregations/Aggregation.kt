package com.exercise.political.speech.dispatchers.aggregations

interface Aggregation {
    /**
     * TODO
     */
    val componentId: String

    /**
     * TODO
     */
    fun execute(): String?
}