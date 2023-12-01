package com.exercise.political.speech.exception

class FileReadException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message: String) : this(message, null)
}