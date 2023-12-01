package com.exercise.political.speech.dispatcher.reader

import java.io.BufferedReader

interface FileReader {
    /**
     * TODO document it
     */
    fun read(input: BufferedReader): List<FileRow>
}