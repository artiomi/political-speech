package com.exercise.political.speech.dispatchers.readers

import java.io.Reader

interface FileReader {
    /**
     * TODO document it
     */
    fun read(reader: Reader): List<FileRow>
}