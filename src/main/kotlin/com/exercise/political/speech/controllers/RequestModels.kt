package com.exercise.political.speech.controllers

import java.net.URI

data class EvaluationRequests(val evaluationRequests: List<EvaluationRequest>)

data class EvaluationRequest(val schema: UriSchema, val uri: URI)

enum class UriSchema(val value: String) {
    FILE("file"), HTTP("http"), HTTPS("https");
}