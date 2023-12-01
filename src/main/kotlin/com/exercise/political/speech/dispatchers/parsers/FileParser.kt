package com.exercise.political.speech.dispatchers.parsers

import com.exercise.political.speech.controllers.EvaluationRequest
import com.exercise.political.speech.dispatchers.readers.FileRow

interface FileParser {
    /**
     * TODO
     */
    fun supports(evaluationRequest: EvaluationRequest):Boolean

    /**
     * TODO
     */
    fun parse(evaluationRequest: EvaluationRequest): List<FileRow>
}
