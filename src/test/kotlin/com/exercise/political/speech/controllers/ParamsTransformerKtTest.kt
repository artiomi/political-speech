package com.exercise.political.speech.controllers

import com.exercise.political.speech.BATCH_ID
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ParamsTransformerKtTest {

    @Test
    fun `successfully transforms list of params to request`() {
        val map = mapOf("url1" to "file:///a", "url2" to "https://github.com")
        val result = paramsToRequest(map, BATCH_ID)
        assertThat(result.evaluationRequests).hasSize(2)
        assertThat(result.evaluationRequests).anyMatch { it.uri.toString() == "https://github.com" && it.schema == UriSchema.HTTPS }
        assertThat(result.evaluationRequests).anyMatch { it.uri.toString() == "file:///a" && it.schema == UriSchema.FILE }
        assertThat(result.batchId).isEqualTo(BATCH_ID)
    }

    @Test
    fun `throw exception during transforming url with not acceptable schema`() {
        val map = mapOf("url1" to "unsupported-schema://github.com")
        assertThatThrownBy { paramsToRequest(map, BATCH_ID) }
            .isExactlyInstanceOf(NoSuchElementException::class.java)
    }
}