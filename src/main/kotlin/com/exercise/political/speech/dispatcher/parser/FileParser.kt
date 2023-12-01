package com.exercise.political.speech.dispatcher.parser

import com.exercise.political.speech.controller.EvaluationRequest
import com.exercise.political.speech.dispatcher.reader.FileRow

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
