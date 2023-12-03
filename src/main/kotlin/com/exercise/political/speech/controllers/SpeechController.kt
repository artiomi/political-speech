package com.exercise.political.speech.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

interface SpeechController {

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
    fun evaluation(@RequestParam params: Map<String, String>): ResponseEntity<Map<String, String?>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}

