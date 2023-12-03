package com.exercise.political.speech.repositories

import com.exercise.political.speech.models.AggregationResult
import com.exercise.political.speech.models.PoliticalSpeech
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface PoliticalSpeechRepo : CrudRepository<PoliticalSpeech, String?> {

    @Query(
        value = """
            select speaker_name as speakerName, count(*) as aggValue 
            from political_speeches ps where date_part('year', occurred_at) = :year
            group by speaker_name order by 2 desc;
        """,
        nativeQuery = true
    )
    fun countSpeakersSpeechesForYearOrderCountDesc(@Param(value = "year") year: Int): List<AggregationResult>

    @Query(
        value = """
            select speaker_name as speakerName, count(*) as aggValue 
            from political_speeches ps where topic = :topic
            group by speaker_name order by 2 desc;
        """,
        nativeQuery = true
    )
    fun countSpeakerSpeechesForTopicOrderCountDesc(@Param(value = "topic") topic: String): List<AggregationResult>

    @Query(
        value = """
            select speaker_name as speakerName, sum(words_count) as aggValue 
            from political_speeches ps
            group by speaker_name order by 2 asc;
        """,
        nativeQuery = true
    )
    fun sumSpeakersWordsOrderSumAsc(): List<AggregationResult>
}