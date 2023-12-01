package com.exercise.political.speech.services

import com.exercise.political.speech.dispatchers.readers.FileRow
import com.exercise.political.speech.models.PoliticalSpeech
import com.exercise.political.speech.repositories.PoliticalSpeechRepo
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDate

@ExtendWith(value = [MockitoExtension::class])
class PoliticalSpeechSvcTest {
    @Mock
    private lateinit var politicalSpeechRepo: PoliticalSpeechRepo
    private lateinit var politicalSpeechSvc: PoliticalSpeechSvc

    @BeforeEach
    fun beforeEach() {
        politicalSpeechSvc = PoliticalSpeechSvc(politicalSpeechRepo)
    }

    @Nested
    inner class CleanAndSaveTest {
        @Test
        fun `throws exception when delete fails`() {
            whenever(politicalSpeechRepo.deleteAll()).thenThrow(RuntimeException::class.java)

            assertThatThrownBy { politicalSpeechSvc.cleanAndSave(emptyList()) }
                .isInstanceOf(RuntimeException::class.java)
            verify(politicalSpeechRepo, never()).saveAll(any<List<PoliticalSpeech>>())
        }

        @Test
        fun `throws exception when save fails`() {
            val fileRow = FileRow("Alexander Abel", "education", LocalDate.parse("2012-10-30"), 5310)
            val speech = PoliticalSpeech("Alexander Abel", "education", LocalDate.parse("2012-10-30"), 5310)
            whenever(politicalSpeechRepo.saveAll(listOf(speech))).thenThrow(RuntimeException::class.java)

            assertThatThrownBy { politicalSpeechSvc.cleanAndSave(listOf(fileRow)) }
                .isInstanceOf(RuntimeException::class.java)
            verify(politicalSpeechRepo).deleteAll()
        }

        @Test
        fun `successfully delete old entries and save new`() {
            val fileRow = FileRow("Alexander Abel", "education", LocalDate.parse("2012-10-30"), 5310)

            assertDoesNotThrow { politicalSpeechSvc.cleanAndSave(listOf(fileRow)) }
            verify(politicalSpeechRepo).deleteAll()
            verify(politicalSpeechRepo)
                .saveAll(argThat<List<PoliticalSpeech>> {
                    size == 1
                            && first().topic == fileRow.topic
                            && first().speakerName == fileRow.speakerName
                            && first().wordsCount == fileRow.wordsCount
                            && first().occurredAt == fileRow.occurredAt
                })
        }
    }
}