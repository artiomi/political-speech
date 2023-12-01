package com.exercise.political.speech.repositories

import com.exercise.political.speech.models.PoliticalSpeech
import org.springframework.data.repository.CrudRepository

interface PoliticalSpeechRepo : CrudRepository<PoliticalSpeech, Long>