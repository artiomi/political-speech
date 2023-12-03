package com.exercise.political.speech.controllers

import java.net.URI

fun paramsToRequest(urlParams: Map<String, String>, batchId: String): EvaluationRequests {
    return urlParams.values
        .map { URI(it) }
        .map { EvaluationRequest(it.scheme.toUriSchema(), it) }
        .let { EvaluationRequests(it, batchId) }
}

fun String?.toUriSchema(): UriSchema {
    return UriSchema.entries.first { it.value == this }
}
