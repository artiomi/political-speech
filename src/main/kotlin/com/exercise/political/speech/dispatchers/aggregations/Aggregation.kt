package com.exercise.political.speech.dispatchers.aggregations

interface Aggregation {
    /**
     * Aggregation unique id, which can be used by clients to filter Aggregation implementations.
     *
     * @return String
     */
    val uniqueId: String

    /**
     * Run SQL aggregation logic and return value with highest/lowest aggregation result.
     * Returns null if no value exists, or multiple entries with same aggregation result exists.
     *
     * @return String!
     */
    fun execute(assembleContext: AssembleContext): String?
}