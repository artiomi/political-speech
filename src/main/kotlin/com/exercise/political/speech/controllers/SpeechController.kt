package com.exercise.political.speech.controllers

import com.exercise.political.speech.dispatchers.EvaluationRequestDispatcher
import com.exercise.political.speech.exceptions.FileReadException
import com.exercise.political.speech.exceptions.ValidationException
import com.exercise.political.speech.validators.SpeechEvaluationValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/speeches")
class SpeechController(
    val requestValidator: SpeechEvaluationValidator,
    val evaluationRequestDispatcher: EvaluationRequestDispatcher
) {
    @GetMapping("/evaluate")
    @ResponseStatus(HttpStatus.OK)
    fun evaluate(@RequestParam params: Map<String, String>) {
        requestValidator.validate(params)
        val request = paramsToRequest(params)
        evaluationRequestDispatcher.execute(request)
    }

}

@ControllerAdvice
class ExceptionAdvice {

    private val log: Logger = LoggerFactory.getLogger(ExceptionAdvice::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException::class, FileReadException::class)
    fun handleProcessingFailure(exception: Throwable): Map<String, String?> {
        log.error("Exception occurred during request process.", exception)
        return mapOf("message" to exception.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable::class)
    fun handleGenericFailure(exception: Throwable): Map<String, String?> {
        log.error("Request process failed.", exception)
        return mapOf("message" to "Request process failed.")
    }
}
