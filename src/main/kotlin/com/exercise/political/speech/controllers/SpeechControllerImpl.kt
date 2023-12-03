package com.exercise.political.speech.controllers

import com.exercise.political.speech.dispatchers.EvaluationRequestDispatcher
import com.exercise.political.speech.exceptions.AssembleException
import com.exercise.political.speech.exceptions.FileReadException
import com.exercise.political.speech.exceptions.ValidationException
import com.exercise.political.speech.validators.SpeechEvaluationValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class SpeechControllerImpl(
    private val requestValidator: SpeechEvaluationValidator,
    private val evaluationRequestDispatcher: EvaluationRequestDispatcher
) : SpeechController {

    override fun evaluation(params: Map<String, String>): ResponseEntity<Map<String, String?>> {
        requestValidator.validate(params)
        val batchId = UUID.randomUUID().toString()
        val request = paramsToRequest(params, batchId)
        val evaluationResult = evaluationRequestDispatcher.dispatch(request)

        return ResponseEntity.ok(evaluationResult)
    }
}

@ControllerAdvice
class ExceptionAdvice {

    private val log: Logger = LoggerFactory.getLogger(ExceptionAdvice::class.java)

    @ExceptionHandler(ValidationException::class, FileReadException::class, AssembleException::class)
    fun handleProcessingFailure(exception: Throwable): ResponseEntity<Map<String, String?>> {
        log.error("Exception occurred during request process.", exception)
        return ResponseEntity.badRequest().body(mapOf("message" to exception.message))
    }

    @ExceptionHandler(Throwable::class)
    fun handleGenericFailure(exception: Throwable): ResponseEntity<Map<String, String>> {
        log.error("Request process failed.", exception)
        return ResponseEntity.badRequest().body(mapOf("message" to "Request process failed."))
    }
}


