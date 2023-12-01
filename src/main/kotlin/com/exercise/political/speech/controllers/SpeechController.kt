package com.exercise.political.speech.controllers

import com.exercise.political.speech.dispatchers.EvaluationRequestDispatcher
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

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationFailure(exception: Throwable): Map<String, String?> {
        log.error("Validation exception occurred.", exception)
        return mapOf("cause" to exception.message)
    }
}
