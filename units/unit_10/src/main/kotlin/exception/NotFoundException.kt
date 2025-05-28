package ru.tbank.exception

open class NotFoundException(override val message: String? = "", override val cause: Throwable? = null) : RuntimeException(message, cause)