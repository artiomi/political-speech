package com.exercise.political.speech

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.core.io.ClassPathResource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

private const val EVALUATION_ENDPOINT = "/evaluation"


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class PoliticalSpeechApplicationIT {
    @Autowired
    private lateinit var mvc: MockMvc

    private fun getUrl(fileName: String) = ClassPathResource(fileName).url.toString()

    @Test
    fun `returns bad request for invalid params`() {
        mvc.perform(
            get(EVALUATION_ENDPOINT)
                .param("test", "value")
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `returns bad request for multiple params with same url`() {
        val url = getUrl("test_01.csv")
        mvc.perform(
            get(EVALUATION_ENDPOINT)
                .param("url1", url)
                .param("url2", url)
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `returns bad request for non existing file`() {
        mvc.perform(
            get(EVALUATION_ENDPOINT)
                .param("url1", "file:/k:/missing_file.csv")
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `returns bad request for file with unsupported separator`() {
        mvc.perform(
            get(EVALUATION_ENDPOINT)
                .param("url1", getUrl("wrong_separator.csv"))
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `returns success for request with multiple files`() {
        mvc.perform(
            get(EVALUATION_ENDPOINT)
                .param("url1", getUrl("test_01.csv"))
                .param("url2", getUrl("test_02.csv"))
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.mostSpeeches").value("Alexander Abel"))
            .andExpect(jsonPath("$.mostSecurity").value("Bernhard Belling"))
            .andExpect(jsonPath("$.leastWordy").value("Caesare Collins"))
    }

    companion object {
        @Container
        @ServiceConnection
        private val postgresContainer = PostgreSQLContainer("postgres:15.4")
    }

}