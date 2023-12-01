package com.exercise.political.speech.dispatchers.readers

import java.io.BufferedReader

interface FileReader {
    /**
     * TODO document it
     */
    fun read(input: BufferedReader): List<FileRow>
}