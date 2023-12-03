package com.exercise.political.speech.dispatchers

import com.exercise.political.speech.controllers.EvaluationRequest
import com.exercise.political.speech.controllers.EvaluationRequests
import com.exercise.political.speech.dispatchers.aggregations.AssembleContext
import com.exercise.political.speech.dispatchers.parsers.FileParser
import com.exercise.political.speech.dispatchers.readers.FileRow
import com.exercise.political.speech.exceptions.FileReadException
import com.exercise.political.speech.services.PoliticalSpeechSvc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EvaluationRequestDispatcher(
    private val fileParsers: List<FileParser>,
    private val politicalSpeechSvc: PoliticalSpeechSvc,
    private val evaluationAggregationsAssembler: EvaluationAggregationsAssembler
) {
    private val log: Logger = LoggerFactory.getLogger(EvaluationRequestDispatcher::class.java)

    fun dispatch(requests: EvaluationRequests): Map<String, String?> {
        log.info("EvaluationRequests received: $requests")
        val rows = readRows(requests)
        politicalSpeechSvc.saveAllInBatch(rows, requests.batchId)
        val assembleResult = evaluationAggregationsAssembler.assemble(AssembleContext(requests.batchId))
        log.info("EvaluationRequests $requests process complete. Result: $assembleResult")

        return assembleResult
    }

    private fun readRows(requests: EvaluationRequests): List<FileRow> {
        val fileRows = mutableListOf<FileRow>()
        for (request in requests.evaluationRequests) {
            val parser = chooseParser(request)
            val rows = parser.parse(request)
            fileRows.addAll(rows)
        }
        return fileRows
    }

    private fun chooseParser(request: EvaluationRequest) =
        fileParsers
            .firstOrNull { it.supports(request) }
            ?: throw FileReadException("Unable to find parser for request: $request")

}