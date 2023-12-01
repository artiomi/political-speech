package com.exercise.political.speech.controller

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ParamsTransformerKtTest {

    @Test
    fun `successfully transforms list of params to request`() {
        val map = mapOf("url1" to "file:///a", "url2" to "file:///b")
        val result = paramsToRequest(map)
        assertThat(result.evaluationRequests).hasSize(2)
        assertThat(result.evaluationRequests).allMatch { it.schema == UriSchema.FILE }
        assertThat(result.evaluationRequests).anyMatch { it.uri.toString() == "file:///a" }
    }

    @Test
    fun `throw exception during transforming url with not acceptable schema`() {
        val map = mapOf("url1" to "https://github.com")
        assertThatThrownBy { paramsToRequest(map) }
            .isExactlyInstanceOf(NoSuchElementException::class.java)
    }
}