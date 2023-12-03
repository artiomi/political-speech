package com.exercise.political.speech.dispatchers.readers

import java.io.Reader

interface FileReader {
    /**
     * Parse received Reader to List of FileRow.
     *
     * @see java.io.Reader
     * @see com.exercise.political.speech.dispatchers.readers.FileRow
     *
     * @return List<FileRow>
     */
    fun read(reader: Reader): List<FileRow>
}