package com.exercise.political.speech.exceptions

class FileReadException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message: String) : this(message, null)
}