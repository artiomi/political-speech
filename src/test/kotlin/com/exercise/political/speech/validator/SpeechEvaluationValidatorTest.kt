package com.exercise.political.speech.validator

import com.exercise.political.speech.exception.ValidationException
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

private const val MAX_ALLOWED_URLS = 2

class SpeechEvaluationValidatorTest {
    private val validator = SpeechEvaluationValidator(MAX_ALLOWED_URLS)

    @Test
    fun `throw exception for empty params`() {
        assertThatThrownBy { validator.validate(emptyMap()) }
            .isExactlyInstanceOf(ValidationException::class.java)
            .hasMessage("Invalid params size.Expected: 2, but received: 0")
    }

    @Test
    fun `throw exception if map size is more than allowed`() {
        val map = mapOf("1" to "1", "2" to "2", "3" to "3")
        assertThat(map.size).isGreaterThan(MAX_ALLOWED_URLS)
        assertThatThrownBy { validator.validate(map) }
            .isExactlyInstanceOf(ValidationException::class.java)
            .hasMessage("Invalid params size.Expected: 2, but received: 3")
    }

    @Test
    fun `no exception is thrown if map size is less than or equal to allowed`() {
        val map = mapOf("url1" to "file:///a", "url22" to "file:///b")
        assertThat(map.size).isLessThanOrEqualTo(MAX_ALLOWED_URLS)
        assertThatNoException().isThrownBy { validator.validate(map) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["1url", "ur1", "urla", "url0", "url123a"])
    fun `throw exception for wrong param name`(paramName: String) {
        val map = mapOf(paramName to "file:///a")
        assertThatThrownBy { validator.validate(map) }
            .isExactlyInstanceOf(ValidationException::class.java)
            .hasMessage("Request is invalid. Parameter:[$paramName] doesn't match pattern:[^url[1-9](\\d+)?\$].")
    }

    @ParameterizedTest
    @ValueSource(strings = ["url1", "url10", "url1234567890"])
    fun `no exception thrown for correct param name`(paramName: String) {
        val map = mapOf(paramName to "file:///a")
        assertThatNoException().isThrownBy { validator.validate(map) }
    }

    @Test
    fun `throw exception for params with duplicate urls`() {
        val map = mapOf("url1" to "file:///a", "url2" to "file:///a")
        assertThatThrownBy { validator.validate(map) }
            .isExactlyInstanceOf(ValidationException::class.java)
            .hasMessage("Request is invalid. The url:[file:///a] is duplicated.")
    }

    @Test
    fun `no exception thrown for params with unique urls`() {
        val map = mapOf("url1" to "file:///a", "url2" to "file:///b")
        assertThatNoException().isThrownBy { validator.validate(map) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["http", "some_schema", ""])
    fun `throw exception for invalid url schema`(schema: String) {
        val map = mapOf("url1" to "$schema:///a")
        assertThatThrownBy { validator.validate(map) }
            .isExactlyInstanceOf(ValidationException::class.java)
            .hasMessage("Request is invalid. Value:[$schema:///a] is not valid. Allowed schemas are:[file]")
    }

    @ParameterizedTest
    @ValueSource(strings = ["file"])
    fun `no exception is thrown for valid url schema`(schema: String) {
        val map = mapOf("url1" to "$schema:///a")
        assertThatNoException().isThrownBy { validator.validate(map) }
    }
}