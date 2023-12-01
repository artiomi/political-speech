package com.exercise.political.speech.controller

import java.net.URI

fun paramsToRequest(urlParams: Map<String, String>): EvaluationRequest {
    return urlParams.values
        .map { URI(it) }
        .map { Payload(it.scheme.toUriSchema(), it) }
        .let { EvaluationRequest(it) }
}


fun String?.toUriSchema(): UriSchema {
    return UriSchema.entries.first { it.value == this }
}
