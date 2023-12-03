package com.exercise.political.speech.dispatchers.parsers

import com.exercise.political.speech.controllers.EvaluationRequest
import com.exercise.political.speech.dispatchers.readers.FileRow

interface FileParser {
    /**
     * Validates if provided request can be handled by the parser.
     *
     * @return Boolean
     */
    fun supports(evaluationRequest: EvaluationRequest): Boolean

    /**
     * Gets content from URI of received request as InputStream and forward stream parsing to a FileReader.
     *
     * @see com.exercise.political.speech.dispatchers.readers.FileReader
     * @see com.exercise.political.speech.dispatchers.readers.FileRow
     *
     * @return List<FileRow>
     */
    fun parse(evaluationRequest: EvaluationRequest): List<FileRow>
}
