package com.exercise.political.speech.controllers

import com.exercise.political.speech.dispatchers.EvaluationRequestDispatcher
import com.exercise.political.speech.exceptions.AssembleException
import com.exercise.political.speech.exceptions.FileReadException
import com.exercise.political.speech.exceptions.ValidationException
import com.exercise.political.speech.validators.SpeechEvaluationValidator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SpeechController(
    private val requestValidator: SpeechEvaluationValidator,
    private val evaluationRequestDispatcher: EvaluationRequestDispatcher
) {

    @Operation(summary = "Evaluates political speeches.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully performed political speeches evaluation.",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        value = """
                            {
                              "mostSpeeches": "speaker1",
                              "mostSecurity": "speaker2",
                              "leastWordy": null
                             }"""
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "400", description = "An error occurred during request execution.",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        value = """
                            {
                              "message": "Request process failed."
                             }"""
                    )]
                )]
            )
        ]
    )
    @Parameters(
        Parameter(
            name = "params",
            description = "A set of query parameters of form: url1,url2,..urln, which points to URL location of a valid CSV file. " +
                    "Allowed schemas are: file, http, https.",
            examples = [ExampleObject(
                name = "urls",
                description = "Urls with allowed schema.",
                value = """
                         {
                          "url1": "file:/D:/data/test-input1.csv",
                          "url2": "https://testhost.io/data/csv/test-input2.csv",
                          "url3": "http://testhost2.io/data/csv/test-input3.csv"
                          }"""
            )],
        )
    )
    @GetMapping("/evaluation")
    fun evaluate(@RequestParam params: Map<String, String>): ResponseEntity<Map<String, String?>> {
        requestValidator.validate(params)
        val request = paramsToRequest(params)
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


