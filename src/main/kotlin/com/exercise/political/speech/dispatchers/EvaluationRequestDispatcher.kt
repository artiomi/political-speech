package com.exercise.political.speech.dispatchers

import com.exercise.political.speech.controllers.EvaluationRequest
import com.exercise.political.speech.controllers.EvaluationRequests
import com.exercise.political.speech.dispatchers.parsers.FileParser
import com.exercise.political.speech.dispatchers.readers.FileRow
import com.exercise.political.speech.exceptions.FileReadException
import com.exercise.political.speech.services.PoliticalSpeechSvc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EvaluationRequestDispatcher(
    val fileParsers: List<FileParser>,
    val politicalSpeechSvc: PoliticalSpeechSvc
) {
    private val log: Logger = LoggerFactory.getLogger(EvaluationRequestDispatcher::class.java)

    fun execute(requests: EvaluationRequests) {
        val rows = readRows(requests)
        politicalSpeechSvc.cleanAndSave(rows)


        //TODO
        // 2. read data from urls, parse and transform to model
        // 3. store in DB, before cleaning old data
        // 4. call executors for each type of param
        // 5 return response
        // 6. tests
    }

    private fun readRows(requests: EvaluationRequests): List<FileRow> {
        val fileRows = mutableListOf<FileRow>()
        for (request in requests.evaluationRequests) {
            log.info("Processing request: $request")
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