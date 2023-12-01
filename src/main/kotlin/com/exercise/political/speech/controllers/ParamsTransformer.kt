package com.exercise.political.speech.controllers

import java.net.URI

fun paramsToRequest(urlParams: Map<String, String>): EvaluationRequests {
    return urlParams.values
        .map { URI(it) }
        .map { EvaluationRequest(it.scheme.toUriSchema(), it) }
        .let { EvaluationRequests(it) }
}


fun String?.toUriSchema(): UriSchema {
    return UriSchema.entries.first { it.value == this }
}
