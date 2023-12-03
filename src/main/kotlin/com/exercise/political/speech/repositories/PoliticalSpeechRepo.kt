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
            from political_speeches ps where batch_id =:batchId and date_part('year', occurred_at) = :year
            group by speaker_name order by 2 desc;
        """,
        nativeQuery = true
    )
    fun countSpeakersSpeechesForYearOrderCountDesc(
        @Param(value = "year") year: Int,
        @Param(value = "batchId") batchId: String
    ): List<AggregationResult>

    @Query(
        value = """
            select speaker_name as speakerName, count(*) as aggValue 
            from political_speeches ps where batch_id =:batchId and topic = :topic
            group by speaker_name order by 2 desc;
        """,
        nativeQuery = true
    )
    fun countSpeakerSpeechesForTopicOrderCountDesc(
        @Param(value = "topic") topic: String,
        @Param(value = "batchId") batchId: String
    ): List<AggregationResult>

    @Query(
        value = """
            select speaker_name as speakerName, sum(words_count) as aggValue 
            from political_speeches ps where batch_id =:batchId 
            group by speaker_name order by 2 asc;
        """,
        nativeQuery = true
    )
    fun sumSpeakersWordsOrderSumAsc(@Param(value = "batchId") batchId: String): List<AggregationResult>
}