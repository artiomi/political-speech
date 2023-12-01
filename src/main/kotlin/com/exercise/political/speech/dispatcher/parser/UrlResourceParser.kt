package com.exercise.political.speech.dispatcher.parser

import com.exercise.political.speech.controller.EvaluationRequest
import com.exercise.political.speech.controller.UriSchema
import com.exercise.political.speech.dispatcher.reader.FileReader
import com.exercise.political.speech.dispatcher.reader.FileRow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component

@Component
class UrlResourceParser(val fileReader: FileReader) : FileParser {
    private val log: Logger = LoggerFactory.getLogger(UrlResourceParser::class.java)

    override fun supports(evaluationRequest: EvaluationRequest) = evaluationRequest.schema == UriSchema.FILE

    override fun parse(evaluationRequest: EvaluationRequest): List<FileRow> {
        log.info("Evaluation request received: $evaluationRequest")

        val urlResource = UrlResource(evaluationRequest.uri)
        return urlResource
            .inputStream
            .use { fileReader.read(it.bufferedReader()) }
    }

}