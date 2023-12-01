package com.exercise.political.speech.dispatchers.parsers

import com.exercise.political.speech.controllers.EvaluationRequest
import com.exercise.political.speech.controllers.UriSchema.*
import com.exercise.political.speech.dispatchers.readers.FileReader
import com.exercise.political.speech.dispatchers.readers.FileRow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component

@Component
class UrlResourceParser(val fileReader: FileReader) : FileParser {
    private val log: Logger = LoggerFactory.getLogger(UrlResourceParser::class.java)
    private val allowedSchemas = listOf(FILE, HTTP, HTTPS)

    override fun supports(evaluationRequest: EvaluationRequest) = allowedSchemas.contains(evaluationRequest.schema)

    override fun parse(evaluationRequest: EvaluationRequest): List<FileRow> {
        log.info("Evaluation request received: $evaluationRequest")

        val urlResource = UrlResource(evaluationRequest.uri)
        return urlResource
            .inputStream
            .bufferedReader()
            .use { fileReader.read(it) }
    }

}