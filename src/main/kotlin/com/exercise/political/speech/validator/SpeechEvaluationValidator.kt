package com.exercise.political.speech.validator

import com.exercise.political.speech.exception.ValidationException
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class SpeechEvaluationValidator(@Value("\${app.speech.validator.max-allowed-urls}") val maxAllowedUrls: Int) {
    private val allowedSchemas = arrayOf("file")
    private val urlValidator = UrlValidator(allowedSchemas)
    private val urlRegex = "^url[1-9](\\d+)?\$"
    private val urlParamPattern: Pattern = Pattern.compile(urlRegex)

    fun validate(requestParams: Map<String, String>) {
        if (requestParams.size > maxAllowedUrls) {
            throw ValidationException("Too much params.Expected: $maxAllowedUrls, but received: ${requestParams.size}")
        }
        val errors = checkUrlParams(requestParams)
        if (errors.isNotEmpty()) {
            throw ValidationException("Request is invalid. ${errors.joinToString("\n")}}")
        }
    }

    private fun checkUrlParams(urlParams: Map<String, String>): List<String> {
        val errors = mutableListOf<String>()
        val viewedUrls = mutableSetOf<String>()
        for (entry in urlParams.entries) {
            when (viewedUrls.contains(entry.value)) {
                true -> errors.add("The url:[${entry.value}] is duplicated.")
                false -> {
                    viewedUrls.add(entry.value)
                    checkParam(entry.toPair())?.let { errors.add(it) }
                }
            }
        }
        return errors
    }

    private fun checkParam(param: Pair<String, String>): String? {
        val result = StringBuilder()
        if (!urlParamPattern.matcher(param.first).matches()) {
            result.append("Parameter:[${param.first}] doesn't match pattern:[$urlRegex].")
        }
        if (!urlValidator.isValid(param.second)) {
            result.append("Value:[${param.second}] is not valid. Allowed schemas are:[${allowedSchemas.joinToString()}]")
        }
        return if (result.isEmpty()) null else result.toString()
    }
}