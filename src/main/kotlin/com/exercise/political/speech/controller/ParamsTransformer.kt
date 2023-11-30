package com.exercise.political.speech.controller

import java.net.URI

data class EvaluationRequest(val payloads: List<Payload>)

data class Payload(val schema: UriSchema, val uri: URI)

enum class UriSchema(val value: String) {
    FILE("file");
}

fun transform(urlParams: Map<String, String>): EvaluationRequest {
    return urlParams.values
        .map { URI(it) }
        .map { Payload(it.scheme.toUriSchema(), it) }
        .let { EvaluationRequest(it) }
}


fun String?.toUriSchema(): UriSchema {
    return UriSchema.entries.first { it.value == this }
}
