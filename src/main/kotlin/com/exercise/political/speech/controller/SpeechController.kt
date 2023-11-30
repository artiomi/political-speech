package com.exercise.political.speech.controller

import com.exercise.political.speech.SpeechEvaluationDispatcher
import com.exercise.political.speech.exception.ValidationException
import com.exercise.political.speech.validator.SpeechEvaluationValidator
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/speeches")
class SpeechController(
    val requestValidator: SpeechEvaluationValidator,
    val speechEvaluationDispatcher: SpeechEvaluationDispatcher
) {
    val log = LoggerFactory.getLogger(SpeechController::class.java)

    @GetMapping("/evaluate")
    @ResponseStatus(HttpStatus.OK)
    fun evaluate(@RequestParam params: Map<String, String>) {
        requestValidator.validate(params)
        val request = transform(params)
        speechEvaluationDispatcher.execute(request)
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationFailure(exception: Throwable): Map<String, String?> {
        log.error("Validation exception occurred.", exception)
        return mapOf("cause" to exception.message)
    }
}

