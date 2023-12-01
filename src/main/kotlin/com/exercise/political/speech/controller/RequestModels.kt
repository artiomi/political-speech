package com.exercise.political.speech.controller

import java.net.URI

data class EvaluationRequest(val payloads: List<Payload>)

data class Payload(val schema: UriSchema, val uri: URI)

enum class UriSchema(val value: String) {
    FILE("file");
}